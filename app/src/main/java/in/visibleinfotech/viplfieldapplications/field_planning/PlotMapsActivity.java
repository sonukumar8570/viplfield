package in.visibleinfotech.viplfieldapplications.field_planning;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class PlotMapsActivity extends FragmentActivity implements OnMapReadyCallback, ScheduleAdapter.OnCheckChangeListener {

    ArrayList<LatLng> plotLatLngs = new ArrayList<>();
    String plotNumber;
    RecyclerView recyclerView;
    MainConstant c;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_activity_plot_maps);

        recyclerView = findViewById(R.id.adviceList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        plotNumber = getIntent().getStringExtra("plot_id");
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
        c = new MainConstant(this);
        new LoadSchedule().execute();
    }


    @Override
    public void onCheckChange(String adviceCode, String adviceName, int status) {
        Intent i = new Intent(PlotMapsActivity.this, UploadActivity.class);
        i.putExtra("plot_id", plotNumber);
        i.putExtra("name", adviceName);
        i.putExtra("code", adviceCode);
        i.putExtra("status", status);

        startActivityForResult(i, 4);
    }

    private LatLngBounds getPolygonLatLngBounds(ArrayList<LatLng> plotLatLngs) {
        final LatLngBounds.Builder centerBuilder = LatLngBounds.builder();
        for (LatLng point : plotLatLngs) {
            centerBuilder.include(point);
        }
        return centerBuilder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            new LoadSchedule().execute();
        }
    }

    private class LoadSchedule extends AsyncTask<String, Void, ArrayList<String>> {
        ArrayList<PlantSchedule> scheduleArrayList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> accounts = new ArrayList<>();
            scheduleArrayList = new ArrayList<>();
            ConnectionStr conStr = new ConnectionStr();

            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect != null) {
                Statement stmt = null;
                try {

                    stmt = connect.createStatement();
                    stmt.setQueryTimeout(5000);
                    String query = "select Lat,Lng from PlotCoordinate where PLOTID = '" + plotNumber + "'";
                    Log.d("myname", query);
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        LatLng latLng = new LatLng(rs.getDouble(1), rs.getDouble(2));
                        plotLatLngs.add(latLng);
                    }
                    query = "select isnull(pp_updStatus,0) pp_updStatus, pp_adviceCode,pp_AdviceDate,p_adviceHn,pp_upddate,p_adviceEng,pp_UpRemark,isnull(pp_status,'No') pp_Status from Vipl_Plantshedule ps inner join Vipl_PlantPlaning pp\n" +
                            "on ps.pp_adviceCode=pp.p_code where pp_plotid = " + plotNumber + " and pp_sitecode = " + c.getSiteCode();
                    Log.d("myname", query);
                    rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        scheduleArrayList.add(new PlantSchedule(rs.getInt(1), plotNumber, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
                    }

                    connect.close();
                } catch (SQLException e) {
                    Log.d("myname", "" + e);
                }
            }
            return accounts;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> accounts) {
            PolygonOptions polygonOptions = new PolygonOptions();
            for (LatLng latLng : plotLatLngs) {
                polygonOptions.add(latLng);
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
         /*   mMap.addPolygon(polygonOptions);
            final LatLngBounds latLngBounds = getPolygonLatLngBounds(plotLatLngs);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));*/
            ScheduleAdapter adapter = new ScheduleAdapter(PlotMapsActivity.this, scheduleArrayList);
            recyclerView.setAdapter(adapter);
        }
    }
}
