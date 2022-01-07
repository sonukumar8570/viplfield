package in.visibleinfotech.viplfieldapplications.docs.unregistered;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class MainActivity extends AppCompatActivity {
    AppCompatAutoCompleteTextView farmerEt, fatherET, villCodeEt, aadharEt;
    String farmer_name, aadharNum, farmer_village_code, G_FatherName;

    Connection connect;
    Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_activity_main_unregistered);

        farmerEt = findViewById(R.id.farmerCodeE);
        villCodeEt = findViewById(R.id.villCodeE);
        fatherET = findViewById(R.id.farmerFatherCodeE);
        aadharEt = findViewById(R.id.aadharIv);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    122);
        }
        uploadButton = findViewById(R.id.uploadBtn);
        new DownloadVillages().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        farmerEt.setText("");
        villCodeEt.setText("");
        fatherET.setText("");
        aadharEt.setText("");
        villCodeEt.requestFocus();
    }


    public void update(View view) {
        G_FatherName = fatherET.getText().toString();
        farmer_name = farmerEt.getText().toString();
        aadharNum = aadharEt.getText().toString();
        if (!aadharNum.isEmpty() && aadharNum.length() != 12) {
            Toast.makeText(this, "Enter valid aadhar number", Toast.LENGTH_SHORT).show();
            aadharEt.requestFocus();
            return;
        }
        if (aadharNum.isEmpty()) {
            aadharNum = "";
        }
        if (farmer_name.isEmpty()) {
            Toast.makeText(this, "Enter Farmer First", Toast.LENGTH_SHORT).show();
            farmerEt.requestFocus();
            return;
        }
        if (G_FatherName.isEmpty()) {
            Toast.makeText(this, "Enter Father name", Toast.LENGTH_SHORT).show();
            fatherET.requestFocus();
            return;
        }
        if (farmer_village_code == null) {
            Toast.makeText(this, "Farmer village missing", Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(this, UploadActivity.class);
        i.putExtra("acc", farmer_name);
        i.putExtra("vill", farmer_village_code);
        i.putExtra("father", G_FatherName);
        i.putExtra("aaa", aadharNum);
        startActivity(i);

    }

    public class DownloadVillages extends AsyncTask<String, String, ArrayList<String>> {
        String msg = "No Internet Connection";
        boolean flag = false, success = false;

        ProgressDialog progress;
        ArrayList docList = new ArrayList();

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
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> villageList = new ArrayList<>();
            try {
                ConnectionStr conStr = new ConnectionStr();
                MainConstant c = new MainConstant(MainActivity.this);
                connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());

                if (connect == null) {
                    success = false;
                    msg = "Connection Failure";
                } else {
                    String query = " select place_id,place_name from VIPL_Place_master where  siteCode=" + c.getSiteCode();
                    Statement stmt = connect.createStatement();
                    Log.d("myname", query);
                    ResultSet rs = stmt.executeQuery(query);
                    msg = "No Villages found";
                    while (rs.next()) {
                        villageList.add(rs.getString(1).trim() + " : " + rs.getString(2).trim());
                        success = true;
                        msg = "Villages loaded successfully";
                    }


                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                msg = e.getMessage();
            }
            return villageList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> zoneList) {

            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (success) {
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.spinner_layout, R.id.spinnerTv, zoneList);
                villCodeEt.setThreshold(1);
                villCodeEt.setAdapter(adapter);
                villCodeEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String farmer = villCodeEt.getText().toString();
                        farmer_village_code = farmer.split(":")[0].trim();
                    }
                });

            }
            progress.dismiss();
        }
    }
}
