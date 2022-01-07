package in.visibleinfotech.viplfieldapplications.brics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_slip.MapsActivity;
import in.visibleinfotech.viplfieldapplications.field_slip.model.Plot;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    TextView mylabel;
    TextView resultTv;
    private GoogleMap mMap;
    boolean mLocationPermissionGranted = false;
    Location mLastKnownLocation;
    FusedLocationProviderClient mFusedLocationClient;

    EditText plotET, landNameTv, bricsCountTv;
    TextView nameTv, villageTv, caneTv, varietyTv, areaTv, remainingTonsTv;
    Plot plot = null;
    ArrayList<LatLng> plotLatLngs = new ArrayList<>();
    PolygonOptions polygonOptions;
    String PL_PlotNumber, zoneName, pl_esttons, estTonWT, checkName = "N", plotVillName, user;
    ArrayList<String> numbersList = new ArrayList<>();
    Button goToMap;
    int i_weigh = 0;
    String plotLandName, bricsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bric_activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
//        getActionBar().setTitle("Field Slip");

        goToMap = findViewById(R.id.goToMapActivity);
        plotET = findViewById(R.id.tagTv);
        nameTv = findViewById(R.id.nameTv);
        villageTv = findViewById(R.id.codeTv);
        caneTv = findViewById(R.id.typeTv);
        varietyTv = findViewById(R.id.varietyTv);
        landNameTv = findViewById(R.id.landNameTv);
        areaTv = findViewById(R.id.areaTv);
        remainingTonsTv = findViewById(R.id.remainingTonsTv);
        bricsCountTv = findViewById(R.id.bricsCount);
        resultTv = findViewById(R.id.resultTv);
        mylabel = findViewById(R.id.mylabelTv);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        user = preferences.getString("username", "Admin");

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
        boolean isInsidePlot = PolyUtil.containsLocation(new LatLng(location.getLatitude(), location.getLongitude()), plotLatLngs, false);
        if (!isInsidePlot) {
            Toast.makeText(this, "Please stand Inside plot first", Toast.LENGTH_SHORT).show();
            return;
        }

        plotLandName = landNameTv.getText().toString();
        bricsCount = bricsCountTv.getText().toString();

        if (plotLandName.isEmpty()) {
            Toast.makeText(this, "Plot land name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bricsCount.isEmpty()) {
            bricsCount = "0";
            bricsCountTv.setText("0");
        }

        new UpdateDetail().execute();
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
            areaTv.setText("");
            mMap.clear();

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
                    String query = "select PL_PlotNumber,PL_PLOTID,PL_GrowerCode,PL_VillageCode,PL_plantationdate,PL_CropId,PL_CaneId,pl_AREA,pl_supplymodeId,pl_landName,PL_enable,Maturity_Days," +
                            "pl_esttons,isnull(PL_PlotBrix,0) PL_PlotBrix,PL_GName,place_name,PL_CropNAMe,PL_VARNAME,zone_id,PL_plotvillcode,PL_VillNAme,pl_extraAllow from VIPL_CanePlotSurvey a inner join Vipl_place_master b on" +
                            " a.PL_VillageCode = b.place_id inner join VIPL_VarietyMaster on a.PL_VarName = Variety_Name where a.pl_sitecode=" + c.getSiteCode() + " and PL_PLOTID = '" + strings[0] + "'";

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
                        int PL_PlotBrix = rs.getInt("PL_PlotBrix");
                        String PL_GName = rs.getString("PL_GName");
                        String place_name = rs.getString("place_name");
                        String PL_CropNAMe = rs.getString("PL_CropNAMe");
                        String PL_VARNAME = rs.getString("PL_VARNAME");
                        String zone_id = rs.getString("zone_id");
                        String PL_plotvillcode = rs.getString("PL_plotvillcode");
                        String PL_VillNAme = rs.getString("PL_VillNAme");
                        String extraAllow = rs.getString("pl_extraAllow");
                        if (extraAllow == null) {
                            extraAllow = "0";
                        }
                        found = true;
                        plot = new Plot(PL_PLOTID, PL_GrowerCode, PL_VillageCode, PL_plantationdate, PL_CropId, PL_CaneId, pl_AREA, pl_supplymodeId, pl_landName, PL_enable, Maturity_Days, pl_esttons, PL_GName, place_name, PL_CropNAMe, PL_VARNAME, zone_id, PL_plotvillcode, PL_VillNAme, PL_PlotBrix);
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

                    query = "select max(I_Weigh) as I_Weigh from VIPL_Indent where I_sitecode = '" + c.getSiteCode() + "' and I_PlotNumber = '" + strings[0] + "'";
                    Log.d("myname", query);
                    rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        i_weigh = rs.getInt("I_Weigh");

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
                villageTv.setText(plot.getPL_VillageCode() + " - " + plot.getPL_VillNAme());
                landNameTv.setText(plot.getPl_landName());
                varietyTv.setText(plot.getPL_VARNAME());
                caneTv.setText(plot.getPL_CropNAMe());
                bricsCountTv.setText("" + plot.getPL_PlotBrix());
                bricsCountTv.setEnabled(true);
                if (plot.getPL_PlotBrix() != 0) {
                    bricsCountTv.setEnabled(false);
                }
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


    class UpdateDetail extends AsyncTask<String, Integer, Integer> {
        ProgressDialog progress;
        boolean flag = false, maxLimit = false;
        String msg;
        int i = 0;

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
        protected Integer doInBackground(String... voids) {
            ConnectionStr conStr = new ConnectionStr();
            MainConstant c = new MainConstant(MainActivity.this);
            if (c.getIp() != null) {
                Log.d("myname", c.getDbUser() + " : " + c.getDbPass() + " : " + c.getDbName() + " : " + c.getIp() + " : " + c.getInstance());
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                if (connect == null) {
                    flag = true;
                } else {
                    String q="";
                    try {
                        Statement stmt = connect.createStatement();

                        //   MainConstant c = new MainConstant(MainActivity.this);

                         q = "update vipl_caneplotsurvey set pl_landName='" + plotLandName + "', PL_PlotBrix=" + bricsCount + ",PL_PlotBrixDate=getdate(),PL_PlotBrixBy='" + c.getUsername() + "' where PL_PLOTID = " + plot.getPL_PLOTID();
                        i = stmt.executeUpdate(q);
                        msg = "updated successfully";

                        connect.close();
                    } catch (SQLException e) {
                        Log.d("myname", "" + e);
                        msg = e.getMessage()+"  "+q;
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


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            if (i > 0) {
                builder.setTitle("Brix updated");

            } else {
                builder.setTitle("Brix Failed");
            }
            builder.setMessage(msg);
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    plot = null;
                    plotET.setText("");
                    nameTv.setText("");
                    villageTv.setText("");
                    landNameTv.setText("");
                    caneTv.setText("");
                    bricsCountTv.setText("0");
                    areaTv.setText("");
                    mMap.clear();

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
