package in.visibleinfotech.viplfieldapplications.field_survey.zone_detail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.ImportActivity;

public class ZoneListActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_zone_list);
        listView = findViewById(R.id.zoneListView);
        new AllZonesFrom().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView z_code = view.findViewById(R.id.z_code_view);
                final TextView z_name = view.findViewById(R.id.z_name_view);
                AlertDialog dialog = new AlertDialog.Builder(ZoneListActivity.this)
                        .setTitle("Information Alert")
                        .setMessage("This is to inform you that If you continue import new zone then All previous zones saved by you willbe removed.")
                        .setPositiveButton("import", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent zoneIntent = new Intent(ZoneListActivity.this, ImportActivity.class);
                                zoneIntent.putExtra("z_code", z_code.getText().toString());
                                zoneIntent.putExtra("z_name", z_name.getText().toString());
                                startActivity(zoneIntent);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        .create();
                dialog.show();

            }
        });
    }

    class AllZonesFrom extends AsyncTask<Void, Integer, ArrayList<Zone>> {
        ProgressDialog progress;
        boolean flag = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ZoneListActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setMax(100);
            progress.setIcon(android.R.drawable.stat_sys_download);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected ArrayList<Zone> doInBackground(Void... voids) {
            ArrayList<Zone> zones = new ArrayList<>();
            ConnectionStr conStr = new ConnectionStr();

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            MainConstant c = new MainConstant(ZoneListActivity.this);
            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect == null) {
                flag = true;
            } else {
                Statement stmt = null;
                try {
                    stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from VIPL_zonemaster");
                    while (rs.next()) {
                        String z_code = rs.getString("z_code");
                        String z_name = rs.getString("z_name");

                        Zone zone = new Zone(z_code, z_name);
                        zones.add(zone);
                    }
                } catch (SQLException e) {

                    Log.d("myname", "" + e);
                }

            }
            return zones;
        }

        @Override
        protected void onPostExecute(ArrayList<Zone> zones) {
            super.onPostExecute(zones);
            if (flag) {
                Toast.makeText(ZoneListActivity.this, "network setting are not correct", Toast.LENGTH_SHORT).show();
            }

            ZoneViewAdapter adapter = new ZoneViewAdapter(ZoneListActivity.this, zones);
            listView.setAdapter(adapter);
            progress.dismiss();
        }
    }
}

