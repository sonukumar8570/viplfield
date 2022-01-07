package in.visibleinfotech.viplfieldapplications.field_survey.survey.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.CustomMapTileProvider;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.PlotDatabase;

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));

        String plotNumber = getIntent().getStringExtra("landName");
        PlotDatabase database = new PlotDatabase(this);
        PolygonOptions options = new PolygonOptions().fillColor(Color.BLUE).geodesic(true);
        ArrayList<LatLng> latLngs = database.getCoordinates(plotNumber);
        for (LatLng latLng : latLngs) {
            options.add(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Your Plot"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 23));
            mMap.addPolygon(options);

        }
    }
}
