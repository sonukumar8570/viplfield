package in.visibleinfotech.viplfieldapplications.field_survey.survey;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.CustomMapTileProvider;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.PlotDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    public static ArrayList<LatLng> list;
    PlotDatabase plotDatabase;
    String plot_id;
    Location mLocation;
    PolygonOptions options;
    Button editBTN, nxtBtn, cancelBtn;
    LinearLayout btnView;
    private GoogleMap mMap;
    TextView accTv;
    float reqAccuracy = 100.0f;


    FusedLocationProviderClient client;
    LocationRequest request;

    LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            mLocation = locationResult.getLastLocation();
            accTv.setText("Req Accuracy = " + reqAccuracy + "\n" + "Mobile Accuracy = " + mLocation.getAccuracy());

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_maps);

        MainConstant constant = new MainConstant(this);
        reqAccuracy = constant.getSurveyAccuracy();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        plot_id = getIntent().getStringExtra("plot_id");
        plotDatabase = new PlotDatabase(this);
        editBTN = findViewById(R.id.editBtn);
        nxtBtn = findViewById(R.id.nextBtn);
        btnView = findViewById(R.id.buttonView);
        cancelBtn = findViewById(R.id.cancelBtn);
        accTv = findViewById(R.id.accuracyTv);
        options = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list = plotDatabase.getCoordinates(plot_id);
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", SphericalUtil.computeArea(list));
                setResult(2, intent);
                finish();
            }
        });
        list = new ArrayList<>();

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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));

        mMap.setPadding(0, 0, 0, 280);
        mMap.setMyLocationEnabled(true);

        //gps = new GPSTracker(this);
        // Add a marker in Sydney and move the camera
        int count = plotDatabase.getNumOfCoordinates(plot_id);
        if (count > 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage(count + " coordinates are already saved for this plot.")
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            plotDatabase.removeCoordinates(plot_id);
                            dialog.dismiss();
                            requestLocation();
                            Toast.makeText(MapsActivity.this, "All previous records are Cleared", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("See on Map", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editBTN.setVisibility(View.VISIBLE);
                            cancelBtn.setVisibility(View.VISIBLE);
                            nxtBtn.setVisibility(View.GONE);
                            btnView.setVisibility(View.GONE);
                            showPreviousMapDialog(plot_id);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list = plotDatabase.getCoordinates(plot_id);
                            Intent intent = new Intent();
                            intent.putExtra("MESSAGE", SphericalUtil.computeArea(list));
                            setResult(2, intent);
                            finish();

                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create();
            alertDialog.show();
        } else {
          requestLocation();
        }

    }
    void requestLocation(){
        request = new LocationRequest();
        request.setInterval(1000 * 5);
        request.setFastestInterval(1000 * 2);

        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(Utils.SMALLEST_DISPLACEMENT);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        request.setMaxWaitTime(1000 * 20);
        client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, callback, Looper.getMainLooper());
        }
    }

    private void showPreviousMapDialog(String plot_id) {
        PolygonOptions options = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);
        ArrayList<LatLng> latLngs = plotDatabase.getCoordinates(plot_id);
        for (LatLng latLng : latLngs) {
            options.add(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at my location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 23));
            mMap.addPolygon(options);

        }

    }

    public void showLocation(View view) {

        if (mLocation.getAccuracy() > reqAccuracy) {
            accuracyAlert(mLocation);
            return;
        }
        LatLng current = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        list.add(current);
        options.add(current);
        mMap.addMarker(new MarkerOptions().position(current).title("" + current));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 23));
        mMap.addPolygon(options);
        MediaPlayer m = MediaPlayer.create(MapsActivity.this, R.raw.click);
        m.start();
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(400);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 23));

    }


    public void reset(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Reset")
                .setMessage("All Marked locations will be removed.")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.clear();
                        mMap.clear();
                        options = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    public void removeLast(View view) {
        if (list.size() > 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Remove last Location")
                    .setMessage("last marked location will be removed.")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(list.size() - 1);
                            mMap.clear();
                            options = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);

                            for (int z = 0; z < list.size(); z++) {
                                LatLng point = list.get(z);
                                options.add(point);
                                mMap.addMarker(new MarkerOptions().position(point).title("Marker at my location"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 23));
                                mMap.addPolygon(options);
                            }

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create();
            alertDialog.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Remove last Location")
                    .setMessage("Cannot remove ")
                    .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create();

            alertDialog.show();
        }
    }

    public void saveCordinates(View view) {
        plotDatabase.addPlotCoordinates(plot_id, list);
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", SphericalUtil.computeArea(list));
        setResult(2, intent);
        finish();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        mLocation = location;
    }


    public void editMap(View view) {
        plotDatabase.removeCoordinates(plot_id);
        mMap.clear();
        options = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);
        Toast.makeText(MapsActivity.this, "All previvious records are Cleared", Toast.LENGTH_SHORT).show();
        editBTN.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
        nxtBtn.setVisibility(View.VISIBLE);
        btnView.setVisibility(View.VISIBLE);
    }

    void accuracyAlert(Location location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Accuracy Alert");
        builder.setTitle("Req Accuracy = " + reqAccuracy + "\n" + "Mobile Accuracy = " + location.getAccuracy());
        builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
}
