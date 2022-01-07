package in.visibleinfotech.viplfieldapplications.docs;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class MainActivity extends AppCompatActivity {
    AppCompatAutoCompleteTextView farmerEt, villCodeEt, aadharEt;
    String farmer_code, farmer_name, farmer_village_name, farmer_village_code, G_FatherName, G_adharCardNo, aadharNum;
    int verified=0;
    Connection connect;
    TextView fatherNameTv;
    Button uploadButton;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_activity_main);

        farmerEt = findViewById(R.id.farmerCodeE);
        villCodeEt = findViewById(R.id.villCodeE);
        fatherNameTv = findViewById(R.id.fatherNameTV);
        aadharEt = findViewById(R.id.aadharIv);
        imageView = findViewById(R.id.imageView);
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
        villCodeEt.requestFocus();
        fatherNameTv.setText("");
        aadharEt.setText("");
     }


    public void update(View view) {
        aadharNum = aadharEt.getText().toString();
        if (!aadharNum.isEmpty() && aadharNum.length() != 12) {
            Toast.makeText(this, "Enter valid aadhar number", Toast.LENGTH_SHORT).show();
            aadharEt.requestFocus();
            return;
        }
        if (aadharNum.isEmpty()) {
            aadharNum = "";
        }
        if (farmer_code == null) {
            Toast.makeText(this, "Search Farmer First", Toast.LENGTH_SHORT).show();
            return;
        }
        if (farmer_village_code == null) {
            Toast.makeText(this, "Farmer village missing", Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(this, UploadActivity.class);
        i.putExtra("acc", farmer_code);
        i.putExtra("vill", farmer_village_code);
        i.putExtra("aaa", aadharNum);
        startActivity(i);

    }

    class DownloadIndent extends AsyncTask<String, Integer, Void> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;

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

            farmer_name = null;
            farmer_village_name = null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(String... voids) {
            MainConstant c = new MainConstant(MainActivity.this);
            if (c.getIp() != null) {
                try {
                    if (connect == null) {
                        flag = true;
                    } else {
                        Statement stmt = connect.createStatement();
                        try {
                            found = false;
                            String q = "select G_FatherName,isnull(G_adharCardNo,0) G_adharCardNo,isNull(G_accVerified,0) verified from VIPL_Accounts_master a inner join VIPL_Place_master b on a.place_id=b.place_id " +
                                    "where account_id='" + farmer_code + "' and b.place_id='" + farmer_village_code + "' and G_factoryCode = " + c.getSiteCode();
                            Log.d("myname", q);
                            ResultSet rs = stmt.executeQuery(q);
                            msg = "No record found";
                            if (rs.next()) {
                                G_FatherName = rs.getString("G_FatherName");
                                G_adharCardNo = rs.getString("G_adharCardNo");
                                verified = rs.getInt("verified");
                                flag = false;
                                found = true;
                                msg = "record found";
                            }

                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                            msg = e.getMessage();
                        }
                    }
                } catch (Exception e) {
                    Log.d("myname", e.toString());
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
            if (found) {
                fatherNameTv.setText(" s/o : " + G_FatherName);
                aadharEt.setText(G_adharCardNo);
                aadharEt.setEnabled(G_adharCardNo.equals("0") || G_adharCardNo.equals(""));
                imageView.setImageResource(verified == 0 ? R.drawable.ic_action_not_connected : R.drawable.ic_action_coonected);
            } else {
                farmer_name = null;
                farmer_code = null;

                villCodeEt.setText("");
                farmerEt.setText("");

            }
            progress.dismiss();
        }
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
                        new DownloadFarmer().execute();
                    }
                });

            }
            progress.dismiss();
        }
    }

    public class DownloadFarmer extends AsyncTask<String, String, ArrayList<String>> {
        String msg = "No Internet Connection";
        boolean flag = false, success = false;

        ProgressDialog progress;

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
            ArrayList<String> farmerList = new ArrayList<>();
            try {
                MainConstant c = new MainConstant(MainActivity.this);

                if (connect == null) {
                    success = false;
                    msg = "Connection Failure";
                } else {
                    String query = "select account_id, account_name from VIPL_Accounts_master where G_factoryCode = " + c.getSiteCode() + " and place_Id =" + farmer_village_code;
                    Statement stmt = connect.createStatement();
                    Log.d("myname", query);
                    ResultSet rs = stmt.executeQuery(query);
                    msg = "No farmer found";
                    while (rs.next()) {
                        farmerList.add(rs.getString(1).trim() + " : " + rs.getString(2).trim());
                        msg = "All Farmer Downloaded";
                        success = true;
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                msg = e.getMessage();
            }
            return farmerList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> zoneList) {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (success) {
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.spinner_layout, R.id.spinnerTv, zoneList);
                farmerEt.setThreshold(1);
                farmerEt.setAdapter(adapter);
                farmerEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String farmer = farmerEt.getText().toString();
                        farmer_code = farmer.split(":")[0].trim();
                        DownloadIndent indent = new DownloadIndent();
                        indent.execute();
                    }
                });
            }
            progress.dismiss();
        }
    }
}
