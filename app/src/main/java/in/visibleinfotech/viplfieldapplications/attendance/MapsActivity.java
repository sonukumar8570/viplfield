package in.visibleinfotech.viplfieldapplications.attendance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    int d, m, y;
    String date;
    TextView dateDialog;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.att_activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Calendar calendar = Calendar.getInstance();
        d = calendar.get(Calendar.DAY_OF_MONTH);
        m = calendar.get(Calendar.MONTH);
        y = calendar.get(Calendar.YEAR);

        dateDialog = findViewById(R.id.dateTv);
        date = String.format(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dateDialog.setText(date);
        new LoadDetail().execute();

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

    }

    public void selectDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String m = "" + month;
                if (month < 10) {
                    m = "0" + month;
                }
                String d = dayOfMonth + "";
                if (dayOfMonth < 10) {
                    d = "0" + dayOfMonth;
                }
                date = year + "-" + m + "-" + d;
                dateDialog.setText(date);
                new LoadDetail().execute();
            }
        }, y, m, d);
        dialog.show();
    }

    class LoadDetail extends AsyncTask<String, Integer, ArrayList<Location>> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;
        String empDate;
        byte[] empImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MapsActivity.this);
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
        protected ArrayList<Location> doInBackground(String... voids) {
            ConnectionStr conStr = new ConnectionStr();
            MainConstant c = new MainConstant(MapsActivity.this);
            ArrayList<Location> records = new ArrayList<>();
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

                        String q = "select lat,longi,date_time from vipl_location  where convert(nvarchar(10),date_time,120) ='"
                                + date + "' and username='" + c.geteCode() + "' order by date_time";

                        Log.d("myname", q);
                        ResultSet rs = stmt.executeQuery(q);
                        while (rs.next()) {
                            double lat = rs.getDouble("lat");
                            double longi = rs.getDouble("longi");
                            String date_time = rs.getString("date_time");

                            Location location = new Location(LocationManager.GPS_PROVIDER);
                            location.setLatitude(lat);
                            location.setLongitude(longi);
                            Calendar c1 = Calendar.getInstance();
                            c1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date_time));
                            System.out.println(c1.getTimeInMillis());
                            location.setTime(c1.getTimeInMillis());
                            records.add(location);
                        }
                    } catch (SQLException | ParseException e) {
                        Log.d("myname", "" + e);
                    }
                }
            } else {
                msg = "Please Edit settings First";
            }
            return records;
        }

        @Override
        protected void onPostExecute(ArrayList<Location> locations) {
            super.onPostExecute(locations);
            mMap.clear();
            PolylineOptions options = new PolylineOptions().color(Color.RED).width(5);
            for (Location location : locations) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                Date date = new Date(location.getTime());
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateFormatted = formatter.format(date);
                mMap.addMarker(new MarkerOptions().position(sydney).title("At : " + dateFormatted));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                options.add(sydney);
            }
            mMap.addPolyline(options);
            progress.dismiss();
        }
    }
}
