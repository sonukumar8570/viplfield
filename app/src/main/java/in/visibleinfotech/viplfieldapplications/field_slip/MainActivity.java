package in.visibleinfotech.viplfieldapplications.field_slip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;
import com.ivrjack.ru01.IvrJackAdapter;
import com.ivrjack.ru01.IvrJackService;
import com.ivrjack.ru01.IvrJackStatus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_slip.model.Plot;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, IvrJackAdapter {
    private static final String TAG = "ru01";
    private static final int REQUEST_FOR_PERMISSION = 1;
    public IvrJackService service;
    String tagNo;
    private BroadcastReceiver volumeBroadcast;
    Thread testReportThread;
    TextView mylabel;
    TextView resultTv;
    private GoogleMap mMap;
    boolean mLocationPermissionGranted = false;
    Location mLastKnownLocation;
    FusedLocationProviderClient mFusedLocationClient;
    AutoCompleteTextView harvesterACTV, transporterACTV;
    EditText slipCountET, plotET;
    TextView nameTv, villageTv, landNameTv, plotVillTv, caneTv, varietyTv, areaTv, remainingTonsTv;
    Plot plot = null;
    ArrayList<String> transporters = new ArrayList<>();
    ArrayList<String> harvesters = new ArrayList<>();
    ArrayList<String> modes = new ArrayList<>();
    ArrayList<LatLng> plotLatLngs = new ArrayList<>();
    PolygonOptions polygonOptions;
    CheckBox checkBox, dateWiseCb;
    String PL_PlotNumber, zoneName, pl_esttons, estTonWT, harvesterCode, harvesterName, transpoterCode, tranporterName, checkName = "N", m_code, m_name, plotVillName, user;
    Spinner modeSpinner;
    BlueToothClass blueToothClass;
    int slipCount = 0, pl_extraAllow = 0, TotalSlip=0,i_weigh = 0, weight = 0, checkCode = 0;
    ArrayList<String> numbersList = new ArrayList<>();
    Button goToMap;
    private String  C_address, C_company_name;

    private String V_IndentType;
    private int PL_Location = 0,C_PlotPerDayIndent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slip_activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
//        getActionBar().setTitle("Field Slip");

        harvesterACTV = findViewById(R.id.harvesterEt);
        transporterACTV = findViewById(R.id.tranporterTv);
        goToMap = findViewById(R.id.goToMapActivity);
        plotET = findViewById(R.id.tagTv);
        nameTv = findViewById(R.id.nameTv);
        villageTv = findViewById(R.id.codeTv);
        caneTv = findViewById(R.id.typeTv);
        varietyTv = findViewById(R.id.varietyTv);
        landNameTv = findViewById(R.id.landNameTv);
        plotVillTv = findViewById(R.id.plotVillTv);
        areaTv = findViewById(R.id.areaTv);
        remainingTonsTv = findViewById(R.id.remainingTonsTv);
        checkBox = findViewById(R.id.checked);
        modeSpinner = findViewById(R.id.modeSpinner);
        resultTv = findViewById(R.id.resultTv);
        slipCountET = findViewById(R.id.slipCount);
        dateWiseCb = findViewById(R.id.dateWiseCb);
        mylabel = findViewById(R.id.mylabelTv);
        MainConstant c = new MainConstant(MainActivity.this);
        user = c.getUsername();
        blueToothClass = new BlueToothClass(this);
        new DownloadHT().execute();
        mLastKnownLocation = null;
        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PL_PlotNumber == null) {
                    Toast.makeText(MainActivity.this, "Seach plot first", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra("plot_id", PL_PlotNumber);
                    startActivity(i);
                }
            }

        });
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();

                        } else {
                            Log.d("myname", "Current location is null. Using defaults.");
                            Log.e("myname", "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.BLUETOOTH}, 12);
            } else {
                service = new IvrJackService(this, this, 1);
                service.open();
            }
        } else {
            service = new IvrJackService(this, this, 1);
            service.open();
        }

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeBroadcast = new VolumnBroadcast();
        registerReceiver(volumeBroadcast, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
    }

    @Override
    public void onConnect(String deviceSn) {
        Log.i(TAG, "on connect");

    }

    @Override
    public void onDisconnect() {
        Log.i(TAG, "on disconnect");

    }

    @Override
    public void onStatusChange(IvrJackStatus status) {
        Log.i(TAG, "on status change: " + status);
        switch (status) {
            case ijsDetecting:
                resultTv.setText("Device detecting...");

                break;
            case ijsRecognized:
                resultTv.setText("Device connected.");

                sendTestReport(true);
                break;
            case ijsUnRecognized:
                resultTv.setText("Device unrecognized.");

                sendTestReport(false);
                break;
            case ijsPlugout:
                resultTv.setText("Device disconnected.");
                break;
        }
    }

    @Override
    public void onInventory(byte[] epc, double rssi) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < epc.length; i++) {
            builder.append(String.format("%02X", epc[i]));
            if ((i + 1) % 4 == 0) builder.append("");
        }
        tagNo = builder.toString();
        resultTv.setText(tagNo);
        new Thread(new StopTask()).start();
        new LoadTagDetail().execute(tagNo);
    }

    @Override
    protected void onDestroy() {
        service.close();
        service = null;

        unregisterReceiver(volumeBroadcast);

        super.onDestroy();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setPadding(0, 0, 0, 80);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
            return;
        } else {
            mMap.setMyLocationEnabled(true);
            polygonOptions = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    122);
        } else {
            mLocationPermissionGranted = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == 122) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocationUI();
                mLocationPermissionGranted = true;
            }
        }
        if (requestCode == REQUEST_FOR_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                service = new IvrJackService(this, this, 1);
                service.open();
            } else {
                Toast.makeText(this, "audio record perssion denied.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            LatLng sydney = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title(new SimpleDateFormat("HH:mm", Locale.US).format(new Date())));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blue_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.bluetooth) {
            showBluetoothDevices();
        }
        if (item.getItemId() == R.id.history) {
            Intent i = new Intent(MainActivity.this, PrintActitvity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private Location getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        mLastKnownLocation = null;
        try {

            if (mLocationPermissionGranted) {
                mLastKnownLocation = mMap.getMyLocation();


            }
        } catch (SecurityException e) {
            Log.d("myname", e.getMessage());
        }
        return mLastKnownLocation;
    }

    public void showLocation(View view) {
        Animation animation2 = new TranslateAnimation(0.0f, 5.0f, 0.0f, 0.0f);
        animation2.setDuration(2000);
        Interpolator oi = new CycleInterpolator(5);
        animation2.setInterpolator(oi);
        if (plot == null) {
            Toast.makeText(this, "Please enter plot_Id first", Toast.LENGTH_SHORT).show();
            plotET.setError("Enter plot number");
            plotET.requestFocus();
            plotET.setAnimation(animation2);
            animation2.startNow();
            return;
        }
        Location location = getDeviceLocation();
        if (location == null) {
            Toast.makeText(this, "Unable to fetch current location", Toast.LENGTH_SHORT).show();
            return;
        }

        if (PL_Location != 1) {
            boolean isInsidePlot = PolyUtil.containsLocation(new LatLng(location.getLatitude(), location.getLongitude()), plotLatLngs, false);
            if (V_IndentType.equals("F") && !isInsidePlot) {
                Toast.makeText(this, "Please stand Inside plot first", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String harvester = harvesterACTV.getText().toString();

        if (harvester.isEmpty()) {
          /*  harvesterACTV.setError("Enter Harvester name");
            harvesterACTV.requestFocus();
            harvesterACTV.setAnimation(animation2);
            animation2.startNow();*/
            harvester = "0 - 0";
        }

        String transporter = transporterACTV.getText().toString();
        if (transporter.isEmpty()) {
            /*transporterACTV.setError("Enter transporter name");
            transporterACTV.requestFocus();
            transporterACTV.setAnimation(animation2);
            animation2.startNow();*/
            transporter = "0 - 0";
        }
        String slipCount = slipCountET.getText().toString();
        if (slipCount.isEmpty()) {
            slipCountET.setError("Enter numer of slips");
            slipCountET.requestFocus();
            slipCountET.setAnimation(animation2);
            animation2.startNow();
        } else {
            int count = Integer.parseInt(slipCount);
            int rem_weight = Integer.parseInt(plot.getPl_esttons()) - i_weigh;
            int i = rem_weight / Integer.parseInt(estTonWT);
            i = i + pl_extraAllow;
            if (C_PlotPerDayIndent < (count + TotalSlip)) {
                Toast.makeText(this, "Per day allowed = " + C_PlotPerDayIndent + " and Already Indent = " + TotalSlip, Toast.LENGTH_SHORT).show();
                return;}
            if ((count > i) && pl_extraAllow <= 0) {
                slipCountET.setError("Enter less number of SLip count");
                slipCountET.requestFocus();
                slipCountET.setAnimation(animation2);
                animation2.startNow();
                Toast.makeText(this, "Allowed = " + i, Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (pl_extraAllow > 0 && count > pl_extraAllow) {
                    slipCountET.setError("Extra allowed is less than SLip count");
                    slipCountET.requestFocus();
                    slipCountET.setAnimation(animation2);
                    animation2.startNow();
                    Toast.makeText(this, "Allowed = " + pl_extraAllow, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    this.slipCount = count;
                    weight = i_weigh + (Integer.parseInt(estTonWT) * count);
                }
            }
        }

        harvesterCode = harvester.split("-")[0].trim();
        harvesterName = harvester.split("-")[1].trim();
        transpoterCode = transporter.split("-")[0].trim();
        tranporterName = transporter.split("-")[1].trim();
        String mode = modeSpinner.getSelectedItem().toString();
        m_code = mode.split("-")[1].trim();
        m_name = mode.split("-")[0].trim();
        if (checkBox.isChecked()) {
            checkCode = 1;
            checkName = "Y";
        }
        new UpdateDetail().execute();
/*
        Select * from vipl_indent
        Select I_PLotNUMBER,I_GROWERCODE,I_GrVillageCode,I_PlantationDate,I_CropId,I_CaneId,i_area,I_CreatedOn,I_CreatedBy,I_SupplyModeId,I_LandName,I_Enable,I_EstTons,I_MDays,I_HarvCode,I_WeighDate,
        I_PrintFlag,I_TransCode,I_EstModeWt,I_GrName,I_GrVillName,I_TransName,I_HarvName,I_CropName,I_VarName,I_ModeName,I_BurnCaneYN,I_BurntCane,I_ZoneCode,I_ZoneName,I_GrBarCode,I_PlotVillCode,I_PlotVillName from vipl_indent*/
    }

    public void searchPlot(View view) {
        String plotNum = plotET.getText().toString();
        if (plotNum.isEmpty()) {
            Toast.makeText(this, "Enter Plot Num", Toast.LENGTH_SHORT).show();
            return;
        }
        new ImportData().execute(plotNum);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;
    }

    public void readTag(View view) {
        new Thread(new StartTask()).start();
    }

    class LoadTagDetail extends AsyncTask<String, Integer, Void> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;
        String transporter;

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

            transporterACTV.setText("");
            harvesterName = null;
            harvesterCode = "0";
            harvesterACTV.setText("");
            transporters = null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        //        select rfid_cont_code,RFID_TRANSPORTER_CODE,b.account_name as trans,c.account_name as harv from VIPL_RFID_ISSUE  a inner join Vipl_Accounts_master b on a.RFID_TRANSPORTER_CODE = b.account_id inner join Vipl_Accounts_master c on a.RFID_CONT_CODE =c.account_id
        @Override
        protected Void doInBackground(String... voids) {
            ConnectionStr conStr = new ConnectionStr();
            MainConstant c = new MainConstant(MainActivity.this);
            if (c.getIp() != null) {
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                if (connect == null) {
                    flag = true;
                    msg = "Unable to connect to the Server";
                } else {
                    Statement stmt = null;
                    try {
                        stmt = connect.createStatement();
                        stmt.setQueryTimeout(5000);
                        String q = "select rfid_cont_code,RFID_TRANSPORTER_CODE,b.account_name as trans,c.account_name as harv from VIPL_RFID_ISSUE  a inner join Vipl_Accounts_master b " +
                                "on a.RFID_TRANSPORTER_CODE = b.account_id inner join Vipl_Accounts_master c on a.RFID_CONT_CODE =c.account_id where RFID_NO='" + voids[0] + "' and a.rfid_sitecode = '" + c.getSiteCode() + "'";
                        Log.d("myname", q);
                        ResultSet rs = stmt.executeQuery(q);
                        if (rs.next()) {
                            found = true;
                            transporter = rs.getString(2) + " - " + rs.getString(3);
                            harvesterCode = rs.getString(1);
                            harvesterName = rs.getString(4);
                            msg = "record found";
                        } else {
                            msg = "No record found";
                        }
                        connect.close();
                    } catch (SQLException e) {
                        Log.d("myname", "" + e);
                    }
                }
            } else {
                msg = "Please Edit settings First";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (flag) {
                Toast.makeText(MainActivity.this, "Setings are not correct", Toast.LENGTH_SHORT).show();
            } else {
                if (found) {
                    transporterACTV.setText(transporter);
                    harvesterACTV.setText(harvesterCode + " - " + harvesterName);
                }
            }
            progress.dismiss();
        }
    }


    private void sendTestReport(final boolean connectResult) {
        // æ£€æŸ¥æ˜¯å¦å·²ç»å‘é€è¿‡æµ‹è¯•æŠ¥å‘Š
        final SharedPreferences sp = getSharedPreferences("ru01", 0);
        boolean flag = sp.getBoolean("TestReportFlag", false);
        if (flag) {
            boolean result = sp.getBoolean("TestReportResult", false);
            if (result || !connectResult) return;
        }

        // æ£€æŸ¥æ˜¯å¦æ­£åœ¨å‘é€æµ‹è¯•æŠ¥å‘Š
        if (testReportThread != null && testReportThread.isAlive()) return;

        // å¼€å¯åŽå°çº¿ç¨‹å‘é€æµ‹è¯•æŠ¥å‘Š
        testReportThread = new Thread() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = sp.edit();
                if (service.sendTestReport()) {
                    // å‘é€æˆåŠŸï¼Œä¿å­˜flag
                    editor.putBoolean("TestReportFlag", true);
                    editor.putBoolean("TestReportResult", connectResult);
                    editor.commit();
                    Log.i("ru01", "send test report success");
                }
            }
        };
        testReportThread.start();
    }


    class StartTask implements Runnable {

        @Override
        public void run() {
            int ret = service.setReadEpcStatus((byte) 1);

        }
    }

    class StopTask implements Runnable {

        @Override
        public void run() {
            int ret = service.setReadEpcStatus((byte) 0);
        }
    }


    private class VolumnBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
    }

    public class ImportData extends AsyncTask<String, String, ArrayList<String>> {
        String msg = "No Internet Connection";
        ProgressDialog progress;
        boolean flag = false, success = false, found = false;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            plot = null;
            nameTv.setText("");
            villageTv.setText("");
            landNameTv.setText("");
            caneTv.setText("");
            harvesterACTV.setText("");
            areaTv.setText("");
            transporterACTV.setText("");
            checkBox.setChecked(false);
            mMap.clear();

            transporters = null;
            harvesters = null;
            plotLatLngs = new ArrayList<>();
            polygonOptions = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            plotLatLngs = new ArrayList<>();

            try {
                ConnectionStr conStr = new ConnectionStr();
                MainConstant c = new MainConstant(MainActivity.this);
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());

                if (connect == null) {
                    success = false;
                    msg = "Connection error";
                } else {
                    flag = true;
                    Statement stmt = connect.createStatement();
                    String query = "select isnull(PL_Location,0) PL_Location,PL_PlotNumber,PL_PLOTID,PL_GrowerCode,PL_VillageCode,PL_plantationdate,PL_CropId,PL_CaneId,pl_AREA,pl_supplymodeId,pl_landName,PL_enable,isnull(Maturity_Days,0) Maturity_Days," +
                            "pl_esttons,PL_GName,place_name,PL_CropNAMe,PL_VARNAME,zone_id,PL_plotvillcode,PL_VillNAme,pl_extraAllow from VIPL_CanePlotSurvey a inner join Vipl_place_master b on" +
                            " a.PL_VillageCode = b.place_id inner join VIPL_VarietyMaster on a.PL_VarName = Variety_Name where isnull(a.PL_PlotEndType,0)=0  and a.pl_sitecode=" + c.getSiteCode() + " and  a.PL_IndentIssue = 1 and PL_PLOTID = '" + strings[0] + "'";

                    Log.d("myname", query);


                    msg = "No record found";

                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        String PL_PLOTID = rs.getString("PL_PLOTID");
                        PL_PlotNumber = rs.getString("PL_PlotNumber");
                        String PL_GrowerCode = rs.getString("PL_GrowerCode");
                        String PL_VillageCode = rs.getString("PL_VillageCode");
                        String PL_plantationdate = rs.getString("PL_plantationdate");
                        String PL_CropId = rs.getString("PL_CropId");
                        String PL_CaneId = rs.getString("PL_CaneId");
                        String pl_AREA = rs.getString("pl_AREA");
                        String pl_supplymodeId = rs.getString("pl_supplymodeId");
                        String pl_landName = rs.getString("pl_landName");
                        String PL_enable = rs.getString("PL_enable");
                        String Maturity_Days = rs.getString("Maturity_Days");
                        pl_esttons = rs.getString("pl_esttons");
                        String PL_GName = rs.getString("PL_GName");
                        String place_name = rs.getString("place_name");
                        String PL_CropNAMe = rs.getString("PL_CropNAMe");
                        String PL_VARNAME = rs.getString("PL_VARNAME");
                        String zone_id = rs.getString("zone_id");
                        String PL_plotvillcode = rs.getString("PL_plotvillcode");
                        String PL_VillNAme = rs.getString("PL_VillNAme");
                        String extraAllow = rs.getString("pl_extraAllow");
                        PL_Location = rs.getInt("PL_Location");

                        if (extraAllow == null) {
                            extraAllow = "0";
                        }
                        pl_extraAllow = Integer.parseInt(extraAllow);
                        found = true;
                        plot = new Plot(PL_PLOTID, PL_GrowerCode, PL_VillageCode, PL_plantationdate, PL_CropId, PL_CaneId, pl_AREA, pl_supplymodeId, pl_landName, PL_enable, Maturity_Days, pl_esttons, PL_GName, place_name, PL_CropNAMe, PL_VARNAME, zone_id, PL_plotvillcode, PL_VillNAme, 0);
                    }
                    query = "select Lat,Lng from PlotCoordinate where sitecode = '" + c.getSiteCode() + "' and PLOTID = '" + PL_PlotNumber + "'";
                    Log.d("myname", query);
                    rs = stmt.executeQuery(query);
                    msg = "No record found";

                    while (rs.next()) {
                        LatLng latLng = new LatLng(rs.getDouble(1), rs.getDouble(2));
                        plotLatLngs.add(latLng);
                        msg = "record found";
                        success = true;
                    }
                    if (plot != null) {
                        query = "select z_name from VIPL_zoneMaster where z_siteCode='" + c.getSiteCode() + "' and z_code= '" + plot.getZone_id() + "'";
                        rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            zoneName = rs.getString(1);
                        }
                        query = "select place_name from Vipl_place_master where sitecode = '" + c.getSiteCode() + "' and place_id= '" + plot.getPL_VillageCode() + "'";
                        Log.d("myname", query);
                        rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            plotVillName = rs.getString(1);

                        }
                    }

                    query = "select max(I_Weigh) as I_Weigh  from VIPL_Indent where I_sitecode = '" + c.getSiteCode() + "' and I_PlotNumber = '" + strings[0] + "'";
                    Log.d("myname", query);
                    rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        i_weigh = rs.getInt("I_Weigh");

                    }
                    query = "select count(*) as TotalSlip  from VIPL_Indent where i_weighdate=convert(varchar(10),getdate(),120) and I_sitecode = '" + c.getSiteCode() + "' and I_PlotNumber = '" + strings[0] + "'";
                    Log.d("myname", query);
                    rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        TotalSlip = rs.getInt("TotalSlip");

                    }
                    query = "select isnull(V_IndentType,'F') V_IndentType from VIPL_Place_master where place_id='" + plot.getPL_plotvillcode() + "'";
                    Log.d("myname", query);
                    rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        V_IndentType = rs.getString("V_IndentType");

                    }




                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> zoneList) {
            Toast.makeText(MainActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (plot != null) {
                areaTv.setText(plot.getPl_AREA());
                nameTv.setText(plot.getPL_GrowerCode() + " - " + plot.getPL_GName());
                plotVillTv.setText(plot.getPL_plotvillcode() + " - " + plot.getPlace_name());
                landNameTv.setText(plot.getPl_landName());
                villageTv.setText(plot.getPL_VillageCode() + " - " + plot.getPL_VillNAme());
                varietyTv.setText(plot.getPL_VARNAME());
                caneTv.setText(plot.getPL_CropNAMe());
                remainingTonsTv.setText("Remaining : " + (Integer.parseInt(pl_esttons) - i_weigh) + " Qntl.");

            }
            if (plotLatLngs.size() > 0) {
                goToMap.setVisibility(GONE);

                for (LatLng latLng : plotLatLngs) {
                    polygonOptions.add(latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at my location"));
                }

                mMap.addPolygon(polygonOptions);
                final LatLngBounds latLngBounds = getPolygonLatLngBounds(plotLatLngs);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
            } else {
                goToMap.setVisibility(View.VISIBLE);

            }
            progress.dismiss();
        }
    }

    private LatLngBounds getPolygonLatLngBounds(ArrayList<LatLng> plotLatLngs) {
        final LatLngBounds.Builder centerBuilder = LatLngBounds.builder();
        for (LatLng point : plotLatLngs) {
            centerBuilder.include(point);
        }
        return centerBuilder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class DownloadHT extends AsyncTask<String, String, ArrayList<String>> {
        String msg = "No Internet Connection";
        ProgressDialog progress;
        boolean flag = false, success = false;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            transporters = new ArrayList<>();
            harvesters = new ArrayList<>();
            modes = new ArrayList<>();
            try {
                ConnectionStr conStr = new ConnectionStr();
                MainConstant c = new MainConstant(MainActivity.this);
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                if (connect == null) {
                    success = false;
                    msg = "Connection error";
                } else {
                    flag = true;
                    Statement stmt = connect.createStatement();
                    String query = "select account_id,account_name from Vipl_Accounts_master where G_AcType='T' and G_factorycode = '" + c.getSiteCode() + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    msg = "No record found";
                    Log.d("myname", query);
                    while (rs.next()) {
                        transporters.add(rs.getString(1) + " - " + rs.getString(2));
                        success = true;
                    }
                    query = "select account_id,account_name from Vipl_Accounts_master where G_AcType='H' and G_factorycode = '" + c.getSiteCode() + "'";
                    rs = stmt.executeQuery(query);
                    msg = "No record found";
                    Log.d("myname", query);
                    while (rs.next()) {
                        harvesters.add(rs.getString(1) + " - " + rs.getString(2));
                        success = true;
                    }
                    query = "select m_name,m_code from VIPL_ModeMaster where M_sitecode = '" + c.getSiteCode() + "' order by m_code ";
                    rs = stmt.executeQuery(query);
                    msg = "No record found";
                    Log.d("myname", query);
                    while (rs.next()) {
                        modes.add(rs.getString(1) + " - " + rs.getString(2));
                        success = true;

                    }


                    query = "select isnull(C_PlotPerDayIndent,0) C_PlotPerDayIndent,C_company_name,C_address from VIPL_Sys1";
                    rs = stmt.executeQuery(query);
                    msg = "No record found";
                    Log.d("myname", query);
                    while (rs.next()) {
                        C_PlotPerDayIndent = rs.getInt(1);
                        C_company_name = rs.getString(2);
                        C_address = rs.getString(3);
                        success = true;
                    }
                    msg = "Harvester and Transporter Downloaded Successfully";
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> zoneList) {
            Toast.makeText(MainActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success) {
                ArrayAdapter<String> harvAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout, R.id.spinnerTv, harvesters);
                ArrayAdapter<String> transAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout, R.id.spinnerTv, transporters);
                ArrayAdapter<String> modesAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout, R.id.spinnerTv, modes);
                harvesterACTV.setAdapter(harvAdapter);
                transporterACTV.setAdapter(transAdapter);
                modeSpinner.setAdapter(modesAdapter);
                harvesterACTV.setThreshold(1);
                transporterACTV.setThreshold(1);

                modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String mode = modeSpinner.getSelectedItem().toString();
                        m_code = mode.split("-")[1].trim();
                        m_name = mode.split("-")[0].trim();
                        new DownloadEstons().execute();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                SharedPreferences preferences = getSharedPreferences("bluetooth", MODE_PRIVATE);
                String name = preferences.getString("name", null);

                if (null != name) {
                    // open bluetooth connection
                    new InitBluetooth().execute(name);
                } else {
                    showBluetoothDevices();
                }
            }
            progress.dismiss();
        }
    }

    public class DownloadEstons extends AsyncTask<String, String, ArrayList<String>> {
        String msg = "No Internet Connection";
        ProgressDialog progress;
        boolean flag = false, success = false;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();
                MainConstant c = new MainConstant(MainActivity.this);
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());

                if (connect == null) {
                    success = false;
                    msg = "Connection error";
                } else {
                    flag = true;
                    Statement stmt = connect.createStatement();
                    String query = "select  isnull(M_IndentWt,80) M_MinWt from VIPL_ModeMaster where M_sitecode = '" + c.getSiteCode() + "' and M_Code= '" + m_code + "'";
                    Log.d("myname", query);
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        estTonWT = rs.getString(1);
                    }

                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> zoneList) {
            progress.dismiss();
        }
    }

    class UpdateDetail extends AsyncTask<String, Integer, Integer> {
        ProgressDialog progress;
        boolean flag = false, maxLimit = false;
        String msg;
        int i = 0;
        String indentNum;
        boolean checkBoxChecked = false;

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
            if (dateWiseCb.isChecked()) {
                checkBoxChecked = true;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(String... voids) {
            ConnectionStr conStr = new ConnectionStr();
            MainConstant c = new MainConstant(MainActivity.this);
            if (c.getIp() != null) {
                Log.d("myname", c.getDbUser() + " : " + c.getDbPass() + " : " + c.getDbName() + " : " + c.getIp() + " : " + c.getInstance());
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                if (connect == null) {
                    flag = true;
                } else {
                    Statement stmt = null;
                    try {
                        int count = 0, maxAllow = 0, previous = 0;
                        String query = "select count(*) as P from vipl_indent where I_sitecode = '" + c.getSiteCode() + "' and I_PLotNUMBER=" + plot.getPL_PLOTID() + " and convert(nvarchar(10),I_CreatedOn,120)='" +
                                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "' and I_SupplyModeId=" + m_code;
                        stmt = connect.createStatement();
                        Log.d("mynane", query);
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            previous = rs.getInt("P");
                        }
                        query = "select isnull(M_INDENT,0) M_INDENT,isnull(M_ExtraIndent,0) M_ExtraIndent from vipl_modemaster where M_sitecode = '" + c.getSiteCode() + "' and M_code=" + m_code;
                        stmt = connect.createStatement();
                        Log.d("mynane", query);
                        rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            maxAllow = rs.getInt("M_INDENT");
                            int abc = rs.getInt("M_ExtraIndent");
                            maxAllow = maxAllow + abc;
                        }
                        if (previous >= maxAllow || slipCount > maxAllow) {
                            maxLimit = true;
                            msg = "Maximum mode limit reached for day";
                            return null;
                        }
                        numbersList = new ArrayList<>();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        for (int k = 0; k < slipCount; k++) {
                            query = "select isnull(max(I_No),0) as i from VIPL_Indent";
                            stmt = connect.createStatement();
                            Log.d("mynane", query);
                            rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                count = rs.getInt(1);
                                msg = "" + count;
                            }/*
                            if (count == 0) {
                                count = count + 1;
                                indentNum = "5" + String.format("%05d", count);
                            } else {*/
                            indentNum = String.valueOf(count + 1);
                            // }

                            if (checkBoxChecked) {
                                Date dt = new Date();
                                Calendar d = Calendar.getInstance();
                                d.setTime(dt);
                                d.add(Calendar.DATE, k);
                                dt = d.getTime();
                                date = new SimpleDateFormat("yyyy-MM-dd").format(dt);
                            }

                            String q = "insert into VIPL_Indent(I_PLotNUMBER,I_No,I_GROWERCODE,I_GrVillageCode,I_PlantationDate,I_CropId,I_CaneId,i_area,I_CreatedOn,I_CreatedBy,I_SupplyModeId,I_LandName,I_Enable,I_EstTons,I_MDays,I_HarvCode,i_weigh,I_WeighDate,\n" +
                                    "I_PrintFlag,I_TransCode,I_EstModeWt,I_GrName,I_GrVillName,I_TransName,I_HarvName,I_CropName,I_VarName,I_ModeName,I_BurnCaneYN,I_BurntCane,I_ZoneCode,I_ZoneName,I_GrBarCode,I_PlotVillCode,I_PlotVillName,i_type,I_sitecode,I_delflag)values ('" +
                                    plot.getPL_PLOTID() + "','" + indentNum + "','" + plot.getPL_GrowerCode() + "','" + plot.getPL_VillageCode() + "','" + plot.getPL_plantationdate() + "','" + plot.getPL_CropId() + "','" + plot.getPL_CaneId()
                                    + "','" + plot.getPl_AREA() + "','" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "','" + user + "','" + m_code + "','" + plot.getPl_landName() + "','" + plot.getPL_enable() + "','" +
                                    plot.getPl_esttons() + "','" + plot.getMaturity_Days() + "','" + harvesterCode + "','" + weight + "','" + date + "','" + 1 + "','" + transpoterCode + "','" + estTonWT +
                                    "','" + plot.getPL_GName() + "','" + plot.getPL_VillNAme() + "','" + tranporterName + "','" + harvesterName + "','" + plot.getPL_CropNAMe() + "','" + plot.getPL_VARNAME() + "','" + m_name + "','" + checkName + "','" + checkCode + "','" +
                                    plot.getZone_id() + "','" + zoneName + "','" + plot.getPL_GrowerCode() + "','" + plot.getPL_plotvillcode() + "','" + plotVillName + "','F','" + c.getSiteCode() + "',1)";
                            Log.d("myname", q);
                            i = stmt.executeUpdate(q);
                            numbersList.add(indentNum);
                            msg = "Data inserted successfully";
if(pl_extraAllow>0) {
    q = "Update vipl_caneplotsurvey set pl_extraAllow = " + (pl_extraAllow - slipCount) + " where PL_PLOTID = " + plot.getPL_PLOTID();
    stmt.executeUpdate(q);
}

                        }
                        connect.close();
                    } catch (SQLException e) {
                        Log.d("myname", "" + e);
                    }
                }
            } else {
                msg = "Please Edit settings First";
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer v) {
            super.onPostExecute(v);
            progress.dismiss();
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (maxLimit) {
                return;
            }
            if (i > 0) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                int printNum = 0;
                for (String number : numbersList) {

                    sendToBluetooth(number, date);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    printNum = printNum + 1;
                    if (dateWiseCb.isChecked()) {
                        Date dt = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(dt);
                        c.add(Calendar.DATE, printNum);
                        dt = c.getTime();
                        date = new SimpleDateFormat("yyyy-MM-dd").format(dt);
                    }
                    slipCount = slipCount - 1;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Indent Creation Successfull");
                builder.setTitle("Your Indent num is : " + indentNum);
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        plot = null;
                        plotET.setText("");
                        nameTv.setText("");
                        villageTv.setText("");
                        landNameTv.setText("");
                        caneTv.setText("");
                        harvesterACTV.setText("");
                        areaTv.setText("");
                        transporterACTV.setText("");
                        checkBox.setChecked(false);
                        mMap.clear();

                        transporters = null;
                        harvesters = null;
                        plotLatLngs = null;
                        polygonOptions = null;


                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }

        }
    }

    void sendToBluetooth(String indentNum, String date) {

        StringBuilder buffer = new StringBuilder();
        blueToothClass.sendHeight(C_company_name + "\n");
        blueToothClass.sendSmall(C_address + "\n");
        blueToothClass.sendBarcode(indentNum);
        buffer.append("Indent Num : ").append(indentNum).append("\n").append("Date : ").append(date).append("\nMode : ").append(m_name).append("\n");
        String name;
        try {
            name = plot.getPL_GName().substring(0, 25);
        } catch (StringIndexOutOfBoundsException e) {
            name = plot.getPL_GName();
        }
        buffer.append("Farmer : ").append(plot.getPL_GrowerCode()).append("\n");
        buffer.append(name);
        buffer.append("\nF-Vill : ").append(plot.getPlace_name());
        //sendBold(name + "\n");

        buffer.append("\nId : ").append(plot.getPL_PLOTID()).append(", Ar : ").append(plot.getPl_AREA()).append("\n").append("Variety : ").append(plot.getPL_VARNAME()).append(",\nCrop: ").append(plot.getPL_CropNAMe()).append("\n").append("PL-Vill : ").append(plot.getPL_VillNAme()).append("\n");
        String t;
        try {
            t = tranporterName.substring(0, 25);
        } catch (StringIndexOutOfBoundsException e) {
            t = tranporterName;
        }
        String h;
        try {
            h = harvesterName.substring(0, 25);
        } catch (StringIndexOutOfBoundsException e) {
            h = harvesterName;
        }
        buffer.append("Harv : ").append(harvesterCode).append("\n").append(h).append("\nTrans : ").append(transpoterCode).append("\n").append(t).append("\n");
        MainConstant c = new MainConstant(MainActivity.this);
        buffer.append("\nCreated By : ").append(c.getUsername());
        buffer.append("\n\n******************\n\n\n\n");
        blueToothClass.sendHeight(buffer.toString());


    }

    private void showBluetoothDevices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select new Bluetooth Device");
        final ListView listView = new ListView(this);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        builder.setView(listView);
        if (mBluetoothAdapter == null) {
            builder.setTitle("No bluetooth adapter available");
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 2);
        }

        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        ArrayList<String> bluetoothList = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            bluetoothList.add(device.getName());

        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences preferences = getSharedPreferences("bluetooth", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String name = listView.getItemAtPosition(position).toString();
                editor.putString("name", name).commit();
                new InitBluetooth().execute(name);
            }
        });
        builder.create();
        builder.show();

    }

    class InitBluetooth extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Connecting to Bluetooth Device", "Please wait");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                blueToothClass.findBT(strings[0]);
                blueToothClass.openBT();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            mylabel.setText(blueToothClass.msg);

        }
    }
}
