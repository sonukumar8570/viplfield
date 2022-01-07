package in.visibleinfotech.viplfieldapplications.field_planning;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_planning.ui.home.Plot;
import in.visibleinfotech.viplfieldapplications.field_planning.ui.home.PlotAdapter;

public class MainActivity extends AppCompatActivity {
    AppCompatAutoCompleteTextView villageEt, nameEt;
    Button searchButton;
    ListView recyclerView;
    String farmerCode, villageCode;
    ProgressBar progressBar;
    MainConstant c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_fragment_home);

        villageEt = findViewById(R.id.villageCodeTv);
        nameEt = findViewById(R.id.farmerNameTv);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.plotsListView);
        progressBar = findViewById(R.id.progressBar);

        c = new MainConstant(this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEt.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter farmer name", Toast.LENGTH_SHORT).show();
                    return;
                }
                farmerCode = nameEt.getText().toString().split(" - ")[0];
                villageCode = villageEt.getText().toString().split(" - ")[0];
                new LoadPlots().execute(villageCode, farmerCode);
            }
        });
        new LoadVillage().execute();

    }

    public void clear(View view) {
        farmerCode=null;
        villageCode=null;
        nameEt.setText("");
        villageEt.setText("");
        recyclerView.setAdapter(null);
    }

    private class LoadVillage extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> villages = new ArrayList<>();
            ConnectionStr conStr = new ConnectionStr();
            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect != null) {
                Statement stmt = null;
                try {
                    stmt = connect.createStatement();
                    stmt.setQueryTimeout(5000);
                    String q = "select place_Id,Place_Name from vipl_place_master where siteCode=" + c.getSiteCode();
                    Log.d("myname", q);
                    ResultSet rs = stmt.executeQuery(q);
                    while (rs.next()) {
                        villages.add(rs.getString(1) + " - " + rs.getString(2));
                    }
                    connect.close();
                } catch (SQLException e) {
                    Log.d("myname", "" + e);
                }
            }
            return villages;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> villages) {
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, villages);
            villageEt.setAdapter(adapter);
            villageEt.setThreshold(1);
            progressBar.setVisibility(View.GONE);
            villageEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    villageCode = villageEt.getText().toString().split(" - ")[0];

                    new LoadFarmers().execute();
                }
            });
        }

        private class LoadFarmers extends AsyncTask<String, Void, ArrayList<String>> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                ArrayList<String> accounts = new ArrayList<>();
                ConnectionStr conStr = new ConnectionStr();
                Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
                if (connect != null) {
                    Statement stmt = null;
                    try {
                        stmt = connect.createStatement();
                        stmt.setQueryTimeout(5000);
                        String q = "select account_id,account_name from vipl_accounts_master where place_id=" + villageCode + " and G_factoryCode =" + c.getSiteCode();
                        Log.d("myname", q);
                        ResultSet rs = stmt.executeQuery(q);
                        while (rs.next()) {
                            accounts.add(rs.getString(1) + " - " + rs.getString(2));
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
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, accounts);
                nameEt.setAdapter(adapter);
                nameEt.setThreshold(1);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class LoadPlots extends AsyncTask<String, Void, ArrayList<Plot>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Plot> doInBackground(String... params) {
            String villageId = params[0];
            String farmerCode = params[1];
            ArrayList<Plot> plots = new ArrayList<>();
            ConnectionStr conStr = new ConnectionStr();
            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect != null) {
                Statement stmt = null;
                try {
                    stmt = connect.createStatement();
                    stmt.setQueryTimeout(5000);
                    String q = "select PL_PlotId,pl_landName from VIPL_CanePlotSurvey " +
                            "where PL_VillageCode = " + villageId + " and pl_sitecode = " + c.getSiteCode() + " and  PL_GrowerCode = " + farmerCode;
                    Log.d("myname", q);
                    ResultSet rs = stmt.executeQuery(q);
                    while (rs.next()) {
                        plots.add(new Plot(rs.getString(1), rs.getString(2)));
                    }
                    connect.close();
                } catch (SQLException e) {
                    Log.d("myname", "" + e);
                }
            }
            return plots;

        }

        @Override
        protected void onPostExecute(final ArrayList<Plot> accounts) {
            PlotAdapter adapter = new PlotAdapter(MainActivity.this, accounts);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);

            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Plot plot = accounts.get(position);
                    Intent i = new Intent(MainActivity.this, PlotMapsActivity.class);
                    i.putExtra("plot_id", plot.getPlotId());
                    startActivity(i);
                }
            });
        }
    }

}
