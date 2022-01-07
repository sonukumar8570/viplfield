package in.visibleinfotech.viplfieldapplications.docs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class UploadActivity extends AppCompatActivity implements LocationListener {

    final int portNumber = 21;
    ImageView photoView, aadharView, bankView, panView, voterView, passportView, otherView;
    String photoImage, aadharImage, bankImage, panImage, voterImage, passportImage, otherImage, aadharNum;
    boolean photo, aadhar, bank, pan, voter, passport, other;
    boolean photoA, aadharA, bankA, panA, voterA, passportA, otherA;
    String photoString, aadharString, bankString, panString, voterString, passportString, otherString;
    String imageFilePath;
    ArrayList<Document> documentArrayList = new ArrayList<>();
    String accountId, villageId;
    int[] docTypeArray = {1, 2, 3, 4, 5, 6, 10};
    Location location;
    String provider;
    boolean mLocationPermissionGranted = false;
    File imageFile;
    private LocationManager locationManager;
    MainConstant c;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_activity_upload);

        c = new MainConstant(this);
        photoView = findViewById(R.id.photoIv);
        aadharView = findViewById(R.id.aadharIv);
        bankView = findViewById(R.id.bankIV);
        panView = findViewById(R.id.panIv);
        voterView = findViewById(R.id.voterIv);
        passportView = findViewById(R.id.passportIv);
        otherView = findViewById(R.id.otherIv);
        documentArrayList = new ArrayList<>();

        accountId = getIntent().getStringExtra("acc");
        villageId = getIntent().getStringExtra("vill");
        aadharNum = getIntent().getStringExtra("aaa");

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        getLocationPermission();
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        new DownloadDocuments().execute();
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
            locationManager.requestLocationUpdates(provider, 400, 1, this);
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
                locationManager.requestLocationUpdates(provider, 400, 1, this);
                location = locationManager.getLastKnownLocation(provider);

            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        getLocationPermission();
        if (mLocationPermissionGranted) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
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

    public void clickImage(View view) {
        switch (view.getId()) {
            case R.id.photoIv:
                if (photoA) {
                    Intent intent = new Intent(this, ImageActivty.class);
                    intent.putExtra("doc_id", photoString);
                    startActivity(intent);
                } else {
                    openCameraIntent(101, 2);
                }
                break;
            case R.id.aadharIv:
                if (aadharA) {
                    Intent intent = new Intent(this, ImageActivty.class);
                    intent.putExtra("doc_id", aadharString);
                    startActivity(intent);
                } else {
                    openCameraIntent(102, 3);
                }
                break;
            case R.id.panIv:
                if (panA) {
                    Intent intent = new Intent(this, ImageActivty.class);
                    intent.putExtra("doc_id", panString);
                    startActivity(intent);
                } else {
                    openCameraIntent(103, 1);
                }
                break;
            case R.id.passportIv:
                if (passportA) {
                    Intent intent = new Intent(this, ImageActivty.class);
                    intent.putExtra("doc_id", passportString);
                    startActivity(intent);
                } else {

                    openCameraIntent(104, 6);
                }
                break;
            case R.id.bankIV:
                if (bankA) {
                    Intent intent = new Intent(this, ImageActivty.class);
                    intent.putExtra("doc_id", bankString);
                    startActivity(intent);
                } else {
                    openCameraIntent(105, 4);
                }
                break;
            case R.id.otherIv:
                if (otherA) {
                    Intent intent = new Intent(this, ImageActivty.class);
                    intent.putExtra("doc_id", otherString);
                    startActivity(intent);
                } else {

                    openCameraIntent(106, 10);
                }
                break;
            case R.id.voterIv:
                if (voterA) {
                    Intent intent = new Intent(this, ImageActivty.class);
                    intent.putExtra("doc_id", otherString);
                    startActivity(intent);
                } else {
                    openCameraIntent(107, 5);
                }
                break;

        }
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

        String imageFileName = c.getSiteCode() + "_" + accountId + "_" + doctype + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        imageFile = image;
        return image;
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getFilesDir();
        File file = new File(filesDir, name + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
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
            Glide.with(this).load(imageFilePath).into(photoView);

            photo = true;
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFilePath).into(aadharView);
            aadhar = true;

        }
        if (requestCode == 103 && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFilePath).into(panView);
            pan = true;
        }
        if (requestCode == 104 && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFilePath).into(passportView);
            passport = true;
        }
        if (requestCode == 105 && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFilePath).into(bankView);
            bank = true;
        }
        if (requestCode == 106 && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFilePath).into(otherView);
            other = true;

        }
        if (requestCode == 107 && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFilePath).into(voterView);
            voter = true;
        }


    }

    public String convertImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        Log.d("mynane", Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        byte[] bytes = byteArrayOutputStream.toByteArray();
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public void update(View view) {
        if (photo) {
//            photoImage = convertImage(((BitmapDrawable) photoView.getDrawable()).getBitmap());
            File file = persistImage((((BitmapDrawable) photoView.getDrawable()).getBitmap()), c.getSiteCode() + "_" + accountId + "_2");
            documentArrayList.add(new Document(accountId, villageId, "2", null, file));
        }

        if (aadhar) {
//            aadharImage = convertImage(((BitmapDrawable) aadharView.getDrawable()).getBitmap());
            File file = persistImage((((BitmapDrawable) aadharView.getDrawable()).getBitmap()), c.getSiteCode() + "_" + accountId + "_3");
            documentArrayList.add(new Document(accountId, villageId, "3", null, file));
        }

        if (pan) {
//            panImage = convertImage(((BitmapDrawable) panView.getDrawable()).getBitmap());
            File file = persistImage((((BitmapDrawable) panView.getDrawable()).getBitmap()), c.getSiteCode() + "_" + accountId + "_1");
            documentArrayList.add(new Document(accountId, villageId, "1", null, file));

        }

        if (passport) {
//            passportImage = convertImage(((BitmapDrawable) passportView.getDrawable()).getBitmap());
            File file = persistImage((((BitmapDrawable) passportView.getDrawable()).getBitmap()), c.getSiteCode() + "_" + accountId + "_6");
            documentArrayList.add(new Document(accountId, villageId, "6", null, file));
        }

        if (bank) {
//            bankImage = convertImage(((BitmapDrawable) bankView.getDrawable()).getBitmap());
            File file = persistImage((((BitmapDrawable) bankView.getDrawable()).getBitmap()), c.getSiteCode() + "_" + accountId + "_4");
            documentArrayList.add(new Document(accountId, villageId, "4", null, file));
        }

        if (other) {
//            otherImage = convertImage(((BitmapDrawable) otherView.getDrawable()).getBitmap());
            File file = persistImage((((BitmapDrawable) otherView.getDrawable()).getBitmap()), c.getSiteCode() + "_" + accountId + "_10");
            documentArrayList.add(new Document(accountId, villageId, "10", null, file));
        }

        if (voter) {
//            voterImage = convertImage(((BitmapDrawable) voterView.getDrawable()).getBitmap());
            File file = persistImage((((BitmapDrawable) voterView.getDrawable()).getBitmap()), c.getSiteCode() + "_" + accountId + "_5");
            documentArrayList.add(new Document(accountId, villageId, "5", null, file));
        }

        if (photoA) photo = photoA;
        if (!photo) {
            Toast.makeText(this, "Photo Image is compulsory", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        if (!aadhar) {
            Toast.makeText(this, "Aadhar Image is compulsory", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bank) {
            Toast.makeText(this, "Bank Image is compulsory", Toast.LENGTH_SHORT).show();
            return;
        }
        */
        if (documentArrayList.size() == 0) {
            Toast.makeText(this, "Nothing to Upload", Toast.LENGTH_SHORT).show();
            return;
        }

        new UpdateGross().execute();
    }

    private void convertByteArray(String encodedImage, ImageView imageView) {

        byte[] val = new byte[encodedImage.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int l = Integer.parseInt(encodedImage.substring(index, index + 2), 16);
            val[i] = (byte) l;
        }
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(val, 0, val.length);
        imageView.setImageBitmap(decodebitmap);
    }

    private class UpdateGross extends AsyncTask<String, String, Integer> {
        String msg = "No Internet Connection";
        boolean flag = false, success = false;
        ProgressDialog progress;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(UploadActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int i = 0;
            try {
                ConnectionStr conStr = new ConnectionStr();
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                if (connect == null) {
                    success = false;
                } else {
                    FTPClient ftp = new FTPClient();
                    ftp.connect(c.getFtpServer(), portNumber);
                    Log.d("myname_371", "Connected. Reply: " + ftp.getReplyString());
                    ftp.login(c.getFtpUser(), c.getFtpPass());
                    ftp.enterLocalPassiveMode(); // important!
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);


                    for (Document document : documentArrayList) {
                        String query = "select isnull(max(doc_id),1000) doc_id from Vipl_DocumentUpload";
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        int max = 0;
                        if (rs.next()) max = rs.getInt(1);
                        FileInputStream in = new FileInputStream(document.getImage());
                        String imagePath = c.getFtpPath() + "doc_" + (max + 1) + ".jpg";
                        Log.d("myname", "uploading    " + imagePath);
                        boolean result = ftp.storeFile(imagePath, in);
                        in.close();
                        if (result) {
                            Log.v("myname", "succeeded");

                            if (location == null) {
                                query = "insert into Vipl_DocumentUpload( doc_id,doc_villcode,doc_grCode,doc_typeCode,doc_user,doc_lat,doc_long,doc_zonecode,Doc_FarType,Doc_aadharNo,Doc_AppVer) values(" + (max + 1) +
                                        ",'" + document.getVillageId() + "','" + document.getAccountId() + "','" + document.getDocTypeCode() + "','" + c.getUsername() + "','"
                                        + 0.0 + "','" + 0.0 + "','" + c.getSiteCode() + "','R','" + aadharNum + "','2')";
                                Log.d("myname", query);
                            } else {
                                query = "insert into Vipl_DocumentUpload( doc_id,doc_villcode,doc_grCode,doc_typeCode,doc_user,doc_lat,doc_long,doc_zonecode,Doc_FarType,Doc_aadharNo,Doc_AppVer) values(" + (max + 1) +
                                        ",'" + document.getVillageId() + "','" + document.getAccountId() + "','" + document.getDocTypeCode() + "','" + c.getUsername() + "','"
                                        + location.getLatitude() + "','" + location.getLongitude() + "','" + c.getSiteCode() + "','R','" + aadharNum + "','2')";
                                Log.d("myname", query);
                            }
                            i = stmt.executeUpdate(query);
                            msg = "Record updated";
                            success = true;
                        } else {
                            break;
                        }


                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                msg = e.getMessage();
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer i) {
            Toast.makeText(UploadActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (i > 0) {
                aadharImage = null;
                panImage = null;
                passportImage = null;
                otherImage = null;
                voterImage = null;
                photoImage = null;
                bankImage = null;
                aadhar = false;
                pan = false;
                passport = false;
                other = false;
                voter = false;
                photo = false;
                bank = false;
                Glide.with(UploadActivity.this).load(android.R.drawable.ic_menu_camera).into(aadharView);
                Glide.with(UploadActivity.this).load(android.R.drawable.ic_menu_camera).into(panView);
                Glide.with(UploadActivity.this).load(android.R.drawable.ic_menu_camera).into(passportView);
                Glide.with(UploadActivity.this).load(android.R.drawable.ic_menu_camera).into(photoView);
                Glide.with(UploadActivity.this).load(android.R.drawable.ic_menu_camera).into(otherView);
                Glide.with(UploadActivity.this).load(android.R.drawable.ic_menu_camera).into(voterView);
                Glide.with(UploadActivity.this).load(android.R.drawable.ic_menu_camera).into(bankView);

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
                builder.setTitle("All Document Uploaded successfully");
                builder.setCancelable(false);
                builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.create();
                builder.show();
            } else {
                Toast.makeText(UploadActivity.this, "Unable to save record at this moment : " + msg, Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }
    }

    class DownloadDocuments extends AsyncTask<String, Integer, Map<Integer, Document>> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;

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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected Map<Integer, Document> doInBackground(String... voids) {
            Map<Integer, Document> documents = new HashMap<>();
            if (c.getIp() != null) {
                try {
                    ConnectionStr conStr = new ConnectionStr();
                    Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());

                    if (connect == null) {
                        flag = true;
                    } else {
                        Statement stmt = connect.createStatement();
                        try {

                            for (int docType : docTypeArray) {
                                String query = "select doc_id from vipl_documentUpload where doc_grcode='" + accountId + "' and doc_TypeCode='" + docType + "' and doc_villCode='" + villageId + "'";
                                ResultSet resultSet = stmt.executeQuery(query);
                                Log.d("myname", query);
                                Document document = null;
                                if (resultSet.next()) {

                                    document = new Document(accountId, villageId, "" + docType, true, resultSet.getString("doc_id"));
                                } else {
                                    document = new Document(accountId, villageId, "" + docType, false, null);
                                }
                                documents.put(docType, document);
                            }

                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                            msg = e.getMessage();
                        }
                    }
                } catch (Exception e) {
                    Log.d("myname", e.toString());
                }
            } else {
                msg = "Please Edit settings First";
            }
            return documents;
        }

        @Override
        protected void onPostExecute(Map<Integer, Document> documents) {
            super.onPostExecute(documents);
            Document panDocument = documents.get(1);
            if (panDocument.getBitmap()) {
                panView.setImageResource(R.drawable.ic_action_tick);
                panA = true;
                panString = panDocument.getDocId();
            }
            Document photoDocument = documents.get(2);
            if (photoDocument.getBitmap()) {
                photoView.setImageResource(R.drawable.ic_action_tick);
                photoA = true;
                photoString = photoDocument.getDocId();
            }
            Document aadharDocument = documents.get(3);
            if (aadharDocument.getBitmap()) {
                aadharView.setImageResource(R.drawable.ic_action_tick);
                aadharA = true;
                aadharString = aadharDocument.getDocId();
            }
            Document bankDocument = documents.get(4);
            if (bankDocument.getBitmap()) {
                bankView.setImageResource(R.drawable.ic_action_tick);
                bankA = true;
                bankString = bankDocument.getDocId();
            }
            Document voterDocument = documents.get(5);
            if (voterDocument.getBitmap()) {
                voterView.setImageResource(R.drawable.ic_action_tick);
                voterA = true;
                voterString = voterDocument.getDocId();
            }
            Document passDocument = documents.get(6);
            if (passDocument.getBitmap()) {
                passportView.setImageResource(R.drawable.ic_action_tick);
                passportA = true;
                passportString = passDocument.getDocId();
            }
            Document otherDocument = documents.get(10);
            if (otherDocument.getBitmap()) {
                otherView.setImageResource(R.drawable.ic_action_tick);
                otherA = true;
                otherString = otherDocument.getDocId();
            }
            progress.dismiss();
        }
    }
}
