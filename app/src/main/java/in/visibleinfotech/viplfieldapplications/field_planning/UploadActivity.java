package in.visibleinfotech.viplfieldapplications.field_planning;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class UploadActivity extends AppCompatActivity implements LocationListener {

    String remark;
    String imageFilePath;
    Location location;
    String provider;
    boolean mLocationPermissionGranted = false;
    MainConstant c;
    private TextView adviceCodeTv;
    private EditText remarkEt;
    private ImageView imageView;
    private boolean photo;
    private LocationManager locationManager;
    private String adviceCode, plotNumber, adviceHn;
    Spinner spinner;
    String text;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_dialog);

        getLocationPermission();

        adviceCodeTv = findViewById(R.id.adviceCodeTv);
        remarkEt = findViewById(R.id.adviceRemarkEt);
        imageView = findViewById(R.id.imageView);
        spinner = findViewById(R.id.remarkUpdateStatus);
        c = new MainConstant(this);
        Button submitButton = findViewById(R.id.submitButton);

        adviceCode = getIntent().getStringExtra("code");
        adviceHn = getIntent().getStringExtra("name");
        plotNumber = getIntent().getStringExtra("plot_id");
        status = getIntent().getIntExtra("status", 0);

        new DownloadImage().execute();

        adviceCodeTv.setText(adviceCode + " " + adviceHn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent(101, Integer.parseInt(adviceCode));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remark = remarkEt.getText().toString();
                if (photo) new UpdateSchedule().execute();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.spinnerTv, new String[]{"Action Taken", "Yes", "No"});
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                text = spinner.getSelectedItem().toString();
                if (!text.equals("Action Taken")) {

                    new UpdateTask().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    122);
        } else {
            mLocationPermissionGranted = true;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            location = locationManager.getLastKnownLocation(provider);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == 122) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // Define the criteria how to select the locatioin provider -> use
                // default
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                location = locationManager.getLastKnownLocation(provider);

            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    private void openCameraIntent(int requestCode, int doctype) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile(doctype);
            } catch (IOException ex) {
                Log.d("myname", "" + ex.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "in.visibleinfotech.viplfieldapplications.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, requestCode);
            }
        }
    }

    private File createImageFile(int doctype) throws IOException {
        String imageFileName = plotNumber + "_" + doctype + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getFilesDir();
        File file = new File(filesDir, name + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFilePath).into(imageView);
            photo = true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("result", 0);
        setResult(4, intent);
        finish();
    }

    private class UpdateSchedule extends AsyncTask<String, Void, Integer> {
        ArrayList<PlantSchedule> scheduleArrayList;

        ProgressDialog progress;
        File file;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(UploadActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();
            file = persistImage((((BitmapDrawable) imageView.getDrawable()).getBitmap()), plotNumber + "_" + adviceCode);

        }

        @Override
        protected Integer doInBackground(String... params) {
            int res = 0;
            ConnectionStr conStr = new ConnectionStr();
            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect != null) {
                Statement stmt = null;
                try {
                    remark = remark + "\nUpload By : " + c.getUsername();
                    stmt = connect.createStatement();
                    stmt.setQueryTimeout(5000);
                    String query;
                    if (location != null) {
                        query = "update Vipl_Plantshedule set pp_updstatus=1, pp_upddate = getdate(), pp_updby= " + c.geteCode() + ",pp_status='No', pp_UpRemark='" +
                                remark + "',pp_updlat=" + location.getLatitude() + ",pp_updlong = " + location.getLongitude() + " where pp_sitecode = " + c.getSiteCode() + " and pp_plotid= " + plotNumber + " and pp_adviceCode=" + adviceCode;
                    } else {
                        query = "update Vipl_Plantshedule set pp_updstatus=1, pp_upddate = getdate(), pp_updby= " + c.geteCode() + ",pp_status='No', pp_UpRemark='" +
                                remark + "' where pp_sitecode = " + c.getSiteCode() + " and pp_plotid= " + plotNumber + " and pp_adviceCode=" + adviceCode;

                    }
                    Log.d("myname", query);
                    res = stmt.executeUpdate(query);
                    connect.close();
                    if (res > 0) {
                        FTPClient ftp = new FTPClient();
                        ftp.connect(c.getFtpServer(), 21);
                        Log.d("myname_371", "Connected. Reply: " + ftp.getReplyString());
                        ftp.login(c.getFtpUser(), c.getFtpPass());
                        ftp.enterLocalPassiveMode(); // important!
                        ftp.setFileType(FTP.BINARY_FILE_TYPE);

                        FileInputStream in = new FileInputStream(file);
                        boolean result = ftp.storeFile(c.getFtpPath() + "sch_" + c.getSiteCode() + "_" + 2122 + "_" + plotNumber + "_" + adviceCode + ".jpg", in);
                        in.close();

                        // Quit from the FTP server.
                        ftp.disconnect();

                    }
                } catch (SQLException e) {
                    Log.d("myname", "" + e);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return res;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progress.dismiss();

            if (file.exists()) file.delete();
            Intent intent = new Intent();
            intent.putExtra("result", integer);
            setResult(4, intent);
            finish();
        }
    }

    private class UpdateTask extends AsyncTask<String, Void, ArrayList<String>> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(UploadActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();

        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ConnectionStr conStr = new ConnectionStr();
            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect != null) {
                Statement stmt = null;
                try {
                    stmt = connect.createStatement();
                    stmt.setQueryTimeout(5000);
                    String query = "update Vipl_Plantshedule set pp_status = '" + text + "' where pp_plotid = " + plotNumber + " and pp_adviceCode =" + adviceCode + " and pp_sitecode = " + c.getSiteCode();
                    Log.d("myname", query);
                    stmt.execute(query);
                    connect.close();
                } catch (SQLException e) {
                    Log.d("myname", "" + e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> accounts) {
            progress.dismiss();
        }
    }

    private class DownloadImage extends AsyncTask<Void, Void, Void> {
        ProgressDialog progress;
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(UploadActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                FTPClient ftp = new FTPClient();
                ftp.connect(c.getFtpServer(), 21);
                Log.d("myname", "Connected. Reply: " + ftp.getReplyString());
                ftp.login(c.getFtpUser(), c.getFtpPass());
                Log.d("myname", "Logged in");
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                Log.d("myname", "Downloading");
                ftp.enterLocalPassiveMode();
                InputStream inStream = ftp.retrieveFileStream(c.getFtpPath() + "sch_" + c.getSiteCode() + "_" + 2122 + "_" + plotNumber + "_" + adviceCode + ".jpg");
                // ftpClient.storeFile(fileName, buffIn);
                bitmap = BitmapFactory.decodeStream(inStream);
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            progress.dismiss();

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}