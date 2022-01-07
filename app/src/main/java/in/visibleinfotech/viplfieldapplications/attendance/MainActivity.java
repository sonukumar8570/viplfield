package in.visibleinfotech.viplfieldapplications.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.visibleinfotech.viplfieldapplications.BuildConfig;
import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class MainActivity extends AppCompatActivity implements LocationListener {
    String slipNum, imei_code, imagePath;
    String imageFilePath = null;
    ImageView mImageView;
    boolean image = false;
    Connection connect;
    TextView statusTv;
    boolean loggedIn = false;
    Button button;
    String emp_id = null;
    String imageName = null;
    String inTime = null;
    Location location;
    String provider;
    boolean mLocationPermissionGranted = false;
    String s_date, outDateStr;
    TextToSpeech t1;
    private LocationManager locationManager;
    MainConstant mainConstant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.att_activity_main);
        mImageView = findViewById(R.id.imageView1);
        statusTv = findViewById(R.id.rew);
        button = findViewById(R.id.button);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @SuppressLint("NewApi")
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.forLanguageTag("hi"));
                }
            }
        });

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
            Toast.makeText(this, "Internet Connection not available", Toast.LENGTH_SHORT).show();
            finish();

        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        mainConstant = new MainConstant(this);

        slipNum = mainConstant.getUsername();
        imei_code = mainConstant.geteCode();
        if (!imei_code.equalsIgnoreCase("-1") && connected) {

            new LoadDetail().execute(imei_code);
        }


    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    122);
        } else {
            mLocationPermissionGranted = true;

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        if (requestCode == 100 && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission  Granted", Toast.LENGTH_SHORT).show();
        } else {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            image = true;
            Glide.with(this).load(imageFilePath).into(mImageView);
        } else {
            image = false;
            Glide.with(this).load(android.R.drawable.ic_menu_camera).into(mImageView);
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

    private void startTrackerService() {
        startService(new Intent(this, TrackingService.class));

//Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

    }

    private void stoptTrackerService() {
        stopService(new Intent(this, TrackingService.class));

//Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking disabled", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public String convertImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        Log.d("myane", "" + byteArrayOutputStream.toByteArray());
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//        return byteArrayOutputStream.toByteArray();
    }

    /*public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }*/
    public void captureImage(View view) {
        if (!slipNum.equals("0"))
            openCameraIntent();

    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("myname", ex.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "in.visibleinfotech.viplfieldapplications.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, 100);
            }
        }
    }

    private File createImageFile() throws IOException {

        String imageFileName = "IMG_" + imei_code + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public void submit(View view) {
        if (!image) {
            Toast.makeText(this, "Capture Image First", Toast.LENGTH_SHORT).show();
            return;

        }
        if (slipNum == null) {
            Toast.makeText(this, "Enter Labour Code", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (loggedIn && s_date != null) {

            String format = "dd-MM-yyyy HH:mm";
            int diffmin = 0;
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                Date dateObj1 = sdf.parse(outDateStr);
                Date dateObj2 = sdf.parse(s_date);
                long diff = dateObj2.getTime() - dateObj1.getTime();
                diffmin = (int) (diff / (60 * 60 * 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("myname", "diff = " + diffmin);
           /* if (diffmin < 4) {
                Toast.makeText(this, "Please Try after 4 hours from login time", Toast.LENGTH_SHORT).show();

                t1.speak("login se chaar ghante ke baad logaut karen", TextToSpeech.QUEUE_FLUSH, null);
            } else {*/
            new UpdateAttendance().execute();

        } else {
           /* if (hour < 7 || hour >= 10) {
                t1.speak("upasthiti keval subah 7 baje se 10 baje ke beech ankit kee jayegee", TextToSpeech.QUEUE_FLUSH, null);
                Toast.makeText(this, "Please mark attendance between 7 AM and 10 AM", Toast.LENGTH_SHORT).show();
                return;
            }*/
            new InsertAttendance().execute();
        }
    }

    public void record(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    private void convertByteArray(String encodedImage, ImageView imageView) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Glide.with(this)
                .asBitmap()
                .load(decodedString)
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(imageView);
    }

    private File persistImage(Bitmap bitmap) {
        File filesDir = getFilesDir();
        File file = new File(filesDir, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return file;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.att_zone_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadDetail extends AsyncTask<String, Integer, Bitmap> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;
        String empDate;
        String empImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
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
        protected Bitmap doInBackground(String... voids) {
            Bitmap bitmap = null;
            MainConstant c = new MainConstant(MainActivity.this);
            if (c.getIp() != null) {
                ConnectionStr conStr = new ConnectionStr();
                connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                if (connect == null) {
                    flag = true;
                    msg = "Unable to connect to the Server";
                } else {
                    Statement stmt = null;
                    try {
                        stmt = connect.createStatement();
                        stmt.setQueryTimeout(5000);
                        String q = "select EMp_id, Emp_InTIme,EMp_InTimeImage,getDate() as s_date from vipl_empAtt where convert(nvarchar(10),EMp_inTime,120) =  convert(nvarchar(10),getDate(),120) and status=0 and Emp_code='" + slipNum
                                + "' and Emp_SiteCode='" + c.getSiteCode() + "' and Emp_Imei='" + imei_code + "' ";

                        Log.d("myname", q);
                        ResultSet rs = stmt.executeQuery(q);
                        if (rs.next()) {
                            found = true;
                            emp_id = rs.getString("EMp_id");
                            empDate = rs.getString("Emp_InTIme");
                            empImage = rs.getString("EMp_InTimeImage");
                            s_date = rs.getString("s_date");
                            msg = "Record found";
                            connect.close();
                            FTPClient ftp = null;


                            ftp = new FTPClient();
                            ftp.connect(c.getFtpServer(), 21);
                            Log.d("myname", "Connected. Reply: " + ftp.getReplyString());

                            ftp.login(c.getFtpUser(), c.getFtpPass());
                            Log.d("myname", "Logged in");
                            ftp.setFileType(FTP.BINARY_FILE_TYPE);
                            Log.d("myname", "Downloading");
                            ftp.enterLocalPassiveMode();
                            InputStream inStream = ftp.retrieveFileStream(c.getFtpPath() + "att_" + emp_id + "_1.jpg");
                            // ftpClient.storeFile(fileName, buffIn);
                            bitmap = BitmapFactory.decodeStream(inStream);
                            ftp.logout();
                            ftp.disconnect();
                        } else {
                            found = false;
                            msg = "No record found";
                        }
                    } catch (Exception e) {
                        Log.d("myname", "" + e);
                    }
                }
            } else {
                msg = "Please Edit settings First";
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap v) {
            super.onPostExecute(v);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (flag) {
                Toast.makeText(MainActivity.this, "Settings are not correct", Toast.LENGTH_SHORT).show();
            } else {

                image = false;
                if (found) {

                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
                    inTime = empDate;
                    try {
                        Date date1 = inputFormat.parse(empDate);
                        outDateStr = outputFormat.format(date1);
                        s_date = outputFormat.format(inputFormat.parse(s_date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.d("myname", "sdate = " + s_date + ", intime = " + inTime);
                    statusTv.setText("आपकी attendance लग चुकी है/\nLogged In at :\n " + outDateStr);

                    t1.speak("Aapki attendance lag chuki hai. Thank you", TextToSpeech.QUEUE_FLUSH, null);
                    loggedIn = true;
                    button.setText("Log Out");
                    button.setBackgroundColor(Color.parseColor("#009688"));
                    mImageView.setImageBitmap(v);
                } else {
                    statusTv.setText("Logged Out");
                    loggedIn = false;
                    button.setText("Log In");
                    s_date = null;
                    button.setBackgroundColor(Color.parseColor("#FF5722"));
                }
            }
            progress.dismiss();
        }
    }

    private class InsertAttendance extends AsyncTask<String, String, Integer> {
        String msg = "No Internet Connection";
        boolean flag = false, success = false;
        ProgressDialog progress;
        String encodingImage;
        Bitmap bitmap;
        File file;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();

            file = persistImage((((BitmapDrawable) mImageView.getDrawable()).getBitmap()));
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int i = 0;
            try {
                ConnectionStr conStr = new ConnectionStr();
                connect = conStr.connectionclasss(mainConstant.getDbUser(), mainConstant.getDbPass(), mainConstant.getDbName(), mainConstant.getIp(), mainConstant.getInstance());
                if (connect == null) {
                    success = false;
                } else {
                    Statement stmt = connect.createStatement();
                    String query = null;
                    if (location != null) {
                        query = "insert into vipl_empAtt(EMP_code,EMP_imei,EMP_intime,EMP_INLAt,EMP_INLONG,status,Emp_siteCode,emp_indate,EMp_appVer) values" +
                                "('" + slipNum + "','" + imei_code + "',getDate(),'"
                                + location.getLatitude() + "','" + location.getLongitude() + "','0','" + mainConstant.getSiteCode() + "',getDate(),'" + BuildConfig.VERSION_CODE + "')";

                    } else {
                        query = "insert into vipl_empAtt(EMP_code,EMP_imei,EMP_intime,EMP_INLAt,EMP_INLONG,status,Emp_siteCode,emp_indate,EMp_appVer) values" +
                                "('" + slipNum + "','" + imei_code + "',getDate(),'"
                                + 0.0 + "','" + 0.0 + "','0','" + mainConstant.getSiteCode() + "',getDate(),'" + BuildConfig.VERSION_CODE + "')";

                    }
                    Log.d("myname", query);
                    i = stmt.executeUpdate(query);
                    if (i > 0) {

                        query = "select emp_id from vipl_empatt where Emp_SiteCode=" + mainConstant.getSiteCode() + " and convert(nvarchar(10),EMp_inTime,120) =  convert(nvarchar(10),getDate(),120) and Emp_Code='" + slipNum + "' and Emp_Imei='" + imei_code + "' and status=0";
                        Log.d("myname", query);
                        ResultSet resultSet = stmt.executeQuery(query);
                        if (resultSet.next()) {
                            String emp_id = resultSet.getString("emp_id");
                            connect.close();
                            Thread.sleep(1000);
                            imageName = emp_id + "_" + 1 + ".jpg";
                            FileInputStream in = new FileInputStream(file);
                            Log.d("myname", "uploading");
                            FTPClient ftp = new FTPClient();
                            ftp.connect(mainConstant.getFtpServer(), 21);
                            Log.d("myname", "Connected. Reply: " + ftp.getReplyString());
                            ftp.login(mainConstant.getFtpUser(), mainConstant.getFtpPass());
                            ftp.enterLocalPassiveMode(); // important!
                            ftp.setFileType(FTP.BINARY_FILE_TYPE);
                            boolean result = ftp.storeFile(mainConstant.getFtpPath() + "att_" + imageName, in);

                            if (result) {
                                Log.v("myname", "succeeded");
                                msg = "Attendance Marked Successfully";
                            } else {
                                msg = "Failed to mark attendance at this moment,\n please check your network connection and try again";
                                connect = conStr.connectionclasss(mainConstant.getDbUser(), mainConstant.getDbPass(), mainConstant.getDbName(), mainConstant.getIp(), mainConstant.getInstance());
                                stmt = connect.createStatement();
                                stmt.execute("Delete from vipl_empatt where emp_id = '" + emp_id + "'");
                                msg = "Record updated";
                            }

                            in.close();
                            ftp.logout();
                            ftp.disconnect();
                        }

                    }

                    success = true;
                }

            } catch (Exception e) {
                Log.d("myname", e.toString());
                msg = "Failed to mark attendance at this moment,\n please check your network connection and try again";
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer i) {
            Toast.makeText(MainActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            if (i > 0) {
                image = false;
                Glide.with(MainActivity.this).load(android.R.drawable.ic_menu_camera).into(mImageView);

                startTrackerService();


                new LoadDetail().execute();
            }
            File fdelete = file;
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + imageFilePath);
                } else {
                    System.out.println("file not Deleted :" + imageFilePath);
                }
            }
            progress.dismiss();
        }
    }

    private class UpdateAttendance extends AsyncTask<String, String, Integer> {
        String msg = "No Internet Connection";
        boolean flag = false, success = false;
        ProgressDialog progress;
        String encodingImage;
        File file;


        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();
            file = persistImage((((BitmapDrawable) mImageView.getDrawable()).getBitmap()));
        }

        @SuppressLint("MissingPermission")
        @Override
        protected Integer doInBackground(String... strings) {
            int i = 0;
            try {

                ConnectionStr conStr = new ConnectionStr();
                connect = conStr.connectionclasss(mainConstant.getDbUser(), mainConstant.getDbPass(), mainConstant.getDbName(), mainConstant.getIp(), mainConstant.getInstance());
                if (connect == null) {
                    success = false;
                } else {

                    Statement stmt = connect.createStatement();
                    LocationDb db = new LocationDb(MainActivity.this);
                    ArrayList<LocationLocalHistory> localHistories = db.getLocations();
                    for (LocationLocalHistory localHistory : localHistories) {
                        String query2 = "Insert into VIPL_Location(username,lat,longi,date_time)" +
                                "values('" + localHistory.getUsername() + "','" + localHistory.getLat() + "','" + localHistory.getLongi() + "','" + localHistory.getDate_time() + "')";
                        stmt.executeUpdate(query2);
                    }

                    db.clearLocationHistory();

                    String query = "Select lat,longi from  VIPL_Location where username = '" + mainConstant.geteCode() + "' " +
                            "and date_time between '" + inTime + "' AND getDate()";
                    Log.d("myname", query);

                    ResultSet resultSet = stmt.executeQuery(query);
                    ArrayList<Location> arrayList = new ArrayList<>();
                    while (resultSet.next()) {
                        Location location = new Location(LocationManager.GPS_PROVIDER);
                        location.setLatitude(resultSet.getDouble("lat"));
                        location.setLongitude(resultSet.getDouble("longi"));
                        arrayList.add(location);
                    }
                    double distance = 0;
                    for (int k = 0; k < arrayList.size() - 2; k++) {
                        Location location = arrayList.get(k);
                        distance = distance + location.distanceTo(arrayList.get(k + 1));
                    }
                    query = "select emp_id from vipl_empatt where Emp_SiteCode=" + mainConstant.getSiteCode() + " and convert(nvarchar(10),EMp_inTime,120) =  convert(nvarchar(10),getDate(),120) and  Emp_Imei='" + imei_code + "' and status=0";
                    resultSet = stmt.executeQuery(query);
                    Log.d("myname", query);

                    if (resultSet.next()) {
                        emp_id = resultSet.getString("emp_id");

                        connect.close();
                        Thread.sleep(1000);
                        imageName = emp_id + "_" + 2 + ".jpg";
                        FTPClient ftp = new FTPClient();
                        ftp.connect(mainConstant.getFtpServer(), 21);
                        Log.d("myname_371", "Connected. Reply: " + ftp.getReplyString());
                        ftp.login(mainConstant.getFtpUser(), mainConstant.getFtpPass());
                        ftp.enterLocalPassiveMode(); // important!
                        ftp.setFileType(FTP.BINARY_FILE_TYPE);
                        FileInputStream in = new FileInputStream(file);
                        Log.d("myname", "uploading");
                        boolean result = ftp.storeFile(mainConstant.getFtpPath() + "att_" + imageName, in);
                        in.close();
                        ftp.logout();
                        ftp.disconnect();
                        location = locationManager.getLastKnownLocation(provider);
                        if (result) {
                            Log.v("myname", "succeeded");
                            connect = conStr.connectionclasss(mainConstant.getDbUser(), mainConstant.getDbPass(), mainConstant.getDbName(), mainConstant.getIp(), mainConstant.getInstance());
                            stmt = connect.createStatement();
                            if (location != null) {
                                query = "Update vipl_empAtt set EMp_outTIme = getDate(), EMp_outdate = getDate()" +
                                        ", EMP_OUTLAT = '" + location.getLatitude() + "', EMP_OUTLONG = '" + location.getLongitude() + "', status = 1" +
                                        ",km='" + String.format("%.3f", distance / 1000) + "'  where Emp_id ='" + emp_id + "'";

                            } else {
                                query = "Update vipl_empAtt set EMp_outTIme = getDate(), EMp_outdate = getDate()" +
                                        ", EMP_OUTLAT = '" + 0.0 + "', EMP_OUTLONG = '" + 0.0 + "', status = 1" +
                                        ",km='" + String.format("%.3f", distance / 1000) + "'  where Emp_id ='" + emp_id + "'";

                            }
                            Log.d("myname", query);
                            i = stmt.executeUpdate(query);
                            msg = "Record updated";
                            success = true;
                            connect.close();
                        } else {
                            msg = "Unable to upload Image at this moment";
                        }
                    }
                }

            } catch (Exception e) {
                Log.d("myname", e.toString());
                msg = "Failed to mark attendance at this moment,\n please check your network connection and try again";
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer i) {
            if (i > 0) {
                image = false;
                Glide.with(MainActivity.this).load(android.R.drawable.ic_menu_camera).into(mImageView);

                stoptTrackerService();
                Toast.makeText(MainActivity.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();

                t1.speak("Logout ho gya hai. Thank you", TextToSpeech.QUEUE_FLUSH, null);

                new LoadDetail().execute();
            } else {
                Toast.makeText(MainActivity.this, "Unable to mark attendance at this moment : " + msg, Toast.LENGTH_SHORT).show();
            }
            File fdelete = file;
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + imageFilePath);
                } else {
                    System.out.println("file not Deleted :" + imageFilePath);
                }
            }
            progress.dismiss();
        }
    }
}

