package in.visibleinfotech.viplfieldapplications.field_survey.survey;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.LogActivity;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.AdvanceSetting;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.PlotDatabase;
import in.visibleinfotech.viplfieldapplications.field_survey.models.Plot;

public class HomeActivty extends AppCompatActivity {

    int uploaded = 0;
    TextView aiseHi;
    MainConstant c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity_home);
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 126);
        }
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 126);
        }


        aiseHi = findViewById(R.id.login_title3);
/*//        Removing the ',' from the end of ZoneDetailAdapter.builder
        StringBuilder place_ids = ZoneDetailAdapter.builder;
        place_ids.deleteCharAt(place_ids.lastIndexOf(","));
        Log.d("myname", place_ids.toString());

//      Inserting the Acount Master detail into Local database
        bar = findViewById(R.id.progressBar);
        ArrayList<AccountMaster> masterArrayList = loadVillages(place_ids.toString());
        database = new AccountMasterDb(this);
        boolean saved = database.addAccoutMaster(masterArrayList);

        */
        c = new MainConstant(this);
        new CheckConnection().execute();
        String name = c.getUsername();
        String zoneCode = c.getCompanyName();
        aiseHi.setText("Login as : " + name + ",\n" + zoneCode);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.survey_zone_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.admin) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.survey_alert_lauout, null, false);
            builder.setTitle("Login");
            builder.setMessage("Enter Admin Password");
            builder.setView(view);
            final EditText et = view.findViewById(R.id.passEt);
            Button loginBtn = view.findViewById(R.id.login);
            Button cancelBtn = view.findViewById(R.id.cancel);
            CheckBox showPassword = view.findViewById(R.id.showPassword);
            builder.setCancelable(false);
            final AlertDialog adialog = builder.create();
            adialog.show();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pas = et.getText().toString().trim();
                    if (pas.equals("vipl123")) {
                        adialog.dismiss();
                        Intent zoneIntent = new Intent(HomeActivty.this, AdvanceSetting.class);
                        startActivity(zoneIntent);
                    } else {
                        et.setError("Enter correct password");
                    }
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adialog.dismiss();
                }
            });
            showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        et.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else {
                        et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
            });
        }
        if (item.getItemId() == R.id.export) {
            exportDB();
        }

        return super.onOptionsItemSelected(item);
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "in.visibleinfotech.viplfieldapplications" + "/databases/" + "VIPL2";

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String backupDBPath = c.getUsername() + "_" + timeStamp + ".db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.d("myname", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void createPlot(View view) {
        Intent i = new Intent(this, CreatePlotActivity.class);
        startActivity(i);
    }

    public void startSurvey(View view) {
        Intent i = new Intent(this, SurveyActivity.class);
        startActivity(i);
    }

    public void showSurvey(View view) {
        Intent i = new Intent(this, ShowSurvey.class);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    public void exportData(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Choose Your Option")
                .setMessage("Do you want to export Data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        checklogin check_Login = new checklogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                        check_Login.execute();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.create();
        dialog.show();

    }

    public void showHelp(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivty.this);
        builder.setTitle("Help");
        View helpView = LayoutInflater.from(HomeActivty.this).inflate(R.layout.help_layout, null, false);
        builder.setView(helpView);
        Button butn = helpView.findViewById(R.id.helpBtn);
        butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://youtu.be/GprhOqFRJng";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        builder.create();
        builder.show();
    }

    public void importData(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Choose Your Option")
                .setMessage("Do you want to Import Data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent i = new Intent(HomeActivty.this, ImportActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.create();
        dialog.show();
    }

    public class checklogin extends AsyncTask<String, String, String> {
        ProgressDialog progress;
        boolean flag = false;
        String msg;
        StringBuilder builder = new StringBuilder();

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(HomeActivty.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected void onPostExecute(String result) {
            writeFileOnInternalStorage(HomeActivty.this, "Queries.txt", builder.toString());

            if (msg != null) {
                Toast.makeText(HomeActivty.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
            if (flag) {
                Toast.makeText(HomeActivty.this, "Network settings are not correct", Toast.LENGTH_SHORT).show();
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivty.this);
            builder1.setTitle("Data Exported");
            builder1.setMessage(uploaded + " " + "Plot Uploaded Successfully");
            builder1.setCancelable(true);
            progress.dismiss();
            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton(
                    "See Logs",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(HomeActivty.this, LogActivity.class);
                            i.putExtra("log", builder.toString());
                            startActivity(i);
                        }
                    });

            AlertDialog alert11 = builder1.create();


            alert11.show();
            //finish();
        }


        @Override
        protected String doInBackground(String... params) {
            int total = 0;
            ConnectionStr conStr = new ConnectionStr();
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect == null) {
                flag = true;
            } else {


                PlotDatabase plotDatabase = new PlotDatabase(HomeActivty.this);
                ArrayList<Plot> plots = plotDatabase.getCompletedPlots();
                for (Plot plot : plots) {
                    String route = plot.getPL_Road();
                    String r_code = "0", r_name = "null";
                    if (route.contains("<->")) {
                        r_code = route.split("<->")[0].trim();
                        r_name = route.split("<->")[1].trim();
                    }
                    int villCode = 0;

                    try {
                        villCode = Integer.parseInt(plot.getPL_VillageCode());

                    } catch (NumberFormatException e) {
                        Log.d("myname", plot.getPL_VillageCode());
                    }
                    String q = "";
                    try {
                        q = "INSERT INTO VIPL_CanePlotSurvey(PL_createdBy,PL_SiteCode, PL_PlotNumber, PL_GrowerCode, PL_VillageCode, PL_PlantationDate,PL_CropId, PL_CaneId, PL_Area," +
                                " PL_LandName, PL_Enable, PL_FieldType, PL_FieldElivation, PL_ActualArea, PL_Road, PL_Enterarea, PL_EntType, PL_HarvMode," +
                                " PL_Plotvillcode,PL_GName, PL_VillName, PL_CropName, PL_VarName,PL_RoadDistance,PL_RoadCode,PL_IrregationTypeCode,PL_IsSoilTested,PL_IsWaterTested," +
                                "PL_WaterSourceId,PL_WaterTypeId,PL_SupplyModeId,PL_SoilId,Pl_CaneRowDistance,PL_TressMalching,PL_CaneSeedBy,PL_SurveyRemark,PL_InterCrop,PL_PrevCrop,PL_KhasraNo," +
                                "PL_CaneOption1,PL_CaneOption2,PL_CaneOption3,PL_CaneOption4,PL_CaneOption5) VALUES" +
                                "('" + c.getUsername() + "'," + c.getSiteCode() + ",'" + plot.getPL_PlotNumber() + "'," + Integer.parseInt(plot.getPL_GrowerCode()) + "," + villCode + ",'" + plot.getPL_PlantationDate() + "'," + Integer.parseInt(plot.getPL_CropId()) + "," + Integer.parseInt(plot.getPL_CaneId()) + "," + Double.parseDouble(plot.getPL_Area()) + ",'" + plot.getPL_LandName() + "'," + Integer.parseInt(plot.getPL_Enable()) + ",'" + plot.getPL_FieldType() + "','" + plot.getPL_FieldElivation() +
                                "'," + Double.parseDouble(plot.getPL_Enterarea()) + ",'" + r_name + "'," + Double.parseDouble(plot.getPL_ActualArea()) + ",'" + plot.getPL_EntType() + "','" + plot.getPL_HarvMode() + "'," + Integer.parseInt(plot.getPL_Plotvillcode()) + ",'" + plot.getPL_GName() + "','" + plot.getPL_VillName() + "','" + plot.getPL_CropName() + "','" + plot.getPL_VarName() + "','" + plot.getPL_RoadDistance() + "'," + r_code + "," + Integer.parseInt(plot.getIrrigationCode()) + ",'" + plot.getIsSoilTested() + "','" + plot.getIsWaterTested() +
                                "'," + Integer.parseInt(plot.getWaterCode()) + "," + Integer.parseInt(plot.getWaterType()) + "," + Integer.parseInt(plot.getSupplymode()) + "," + Integer.parseInt(plot.getSoilCode()) + ",'" + plot.getRowDirection() + "','" + plot.getTressMalching() + "'," +
                                "'" + plot.getSeed() + "','" + plot.getRemark() + "','" + plot.getInterCrop() + "','" + plot.getPrevCrop() + "','" + plot.getKashraNum() + "','" + plot.getDev1Option() + "'," +
                                "'" + plot.getDev2Option() + "','" + plot.getDev3Option() + "','" + plot.getDev4Option() + "','" + plot.getDev5Option() + "')";
                        Statement stmt = connect.createStatement();

                        Log.d("myname", q);
                        builder.append("\nPlot : \n");
                        builder.append(q);
                        uploaded = stmt.executeUpdate(q);
                        int action = 0;
                        try {
                            for (LatLng latLng : plotDatabase.getCoordinates(plot.getPL_PlotNumber())) {
                                String insertNewUserSQL2 = "INSERT INTO PlotCoordinate(VILLID,FarmerID ,PLOTID,LAT,LNG) VALUES (" + Integer.parseInt(plot.getPL_VillageCode()) + "," + Integer.parseInt(plot.getPL_GrowerCode()) + ",'" + plot.getPL_PlotNumber() + "'," + latLng.latitude + "," + latLng.longitude + ");";
                                Log.d("myname", insertNewUserSQL2);
                                builder.append("\nMap : " + insertNewUserSQL2 + " \n");
                                action = stmt.executeUpdate(insertNewUserSQL2);
                            }
                        } catch (SQLException e) {
                            Log.d("myname", e.getMessage());
                            builder.append("\nError : " + e.getMessage() + " \n");
                            msg = e.getMessage() + "  -  " + q;
                        }
                        if (uploaded > 0 && action > 0) {
                            plotDatabase.updateUpload(plot.getPL_PlotNumber());
                            total++;
                        }
                    } catch (NumberFormatException e) {
                        Log.d("myname", e.toString());
                        msg = e.getMessage() + " - " + q;
                        builder.append("\nError : " + e.getMessage() + " \n");
                    } catch (SQLException e) {
                        Log.d("myname", e.getMessage());
                        msg = e.getMessage();
                        builder.append("\nError : " + e.getMessage() + " \n");
                    }
                }
            }


            // Toast.makeText(MainActivity.this, "Data Exported Successfully", Toast.LENGTH_SHORT).show();


            return "" + total;
        }

    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody) {
//        File dir = new File(mcoContext.getFilesDir(), "mydir");
//        if(!dir.exists()){
//            dir.mkdir();
//        }
//
//        try {
//            File gpxfile = new File(dir, sFileName);
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(sBody);
//            writer.flush();
//            writer.close();
//            Toast.makeText(mcoContext, ""+dir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        } catch (Exception e){
//            e.printStackTrace();
//        }

        File root = android.os.Environment.getExternalStorageDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, sFileName);

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(sBody);
            pw.flush();
            pw.close();

            Toast.makeText(mcoContext, "" + dir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            Log.i(TAG, "******* File not found. Did you" +
//                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        }
    }

    private class CheckConnection extends AsyncTask<Void, Void, Connection> {

        @Override
        protected Connection doInBackground(Void... voids) {
            ConnectionStr conStr = new ConnectionStr();
            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());

            return connect;
        }

        @Override
        protected void onPostExecute(Connection connection) {
            super.onPostExecute(connection);
            ImageView imageView = findViewById(R.id.imageView);
            if (connection != null) {
                imageView.setImageResource(R.drawable.ic_action_coonected);
            } else {
                imageView.setImageResource(R.drawable.ic_action_not_connected);
            }
        }
    }
}

