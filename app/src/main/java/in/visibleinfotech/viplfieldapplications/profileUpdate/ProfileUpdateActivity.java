package in.visibleinfotech.viplfieldapplications.profileUpdate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class ProfileUpdateActivity extends AppCompatActivity {

    AutoCompleteTextView p_mobileEt, p_nameEt, p_villageEt, p_aadharEt;

    Connection connect;
    String farmer_village_code, farmer_code;
    private String aadharNum, mobileNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        p_mobileEt = findViewById(R.id.p_mobileEt);
        p_nameEt = findViewById(R.id.p_profileNameEt);
        p_villageEt = findViewById(R.id.p_villageEt);
        p_aadharEt = findViewById(R.id.p_aadharEt);
        new DownloadVillages().execute();
    }

    public void saveUpdate(View view) {
        mobileNum = p_mobileEt.getText().toString();
        aadharNum = p_aadharEt.getText().toString();

        if (mobileNum.length() < 10) {
            p_mobileEt.setError("Mobile num is not valid");
            return;
        }

        new UpdateProfileDetail().execute();
    }

    public class DownloadVillages extends AsyncTask<String, String, ArrayList<String>> {
        String msg = "No Internet Connection";
        boolean flag = false, success = false;

        ProgressDialog progress;

        @SuppressLint("MissingPermission")
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(ProfileUpdateActivity.this);
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
                MainConstant c = new MainConstant(ProfileUpdateActivity.this);
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

            Toast.makeText(ProfileUpdateActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (success) {
                ArrayAdapter adapter = new ArrayAdapter(ProfileUpdateActivity.this, R.layout.spinner_layout, R.id.spinnerTv, zoneList);
                p_villageEt.setThreshold(1);
                p_villageEt.setAdapter(adapter);
                p_villageEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String farmer = p_villageEt.getText().toString();
                        farmer_village_code = farmer.split(":")[0].trim();
                        new ProfileUpdateActivity.DownloadFarmer().execute();
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
            progress = new ProgressDialog(ProfileUpdateActivity.this);
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
                MainConstant c = new MainConstant(ProfileUpdateActivity.this);

                if (connect == null) {
                    success = false;
                    msg = "Connection Failure";
                } else {
                    String query = "select account_id, account_name from VIPL_Accounts_master where G_AcType = 'F' and G_factoryCode = " + c.getSiteCode() + " and place_Id =" + farmer_village_code;
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
            Toast.makeText(ProfileUpdateActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (success) {
                ArrayAdapter adapter = new ArrayAdapter(ProfileUpdateActivity.this, R.layout.spinner_layout, R.id.spinnerTv, zoneList);
                p_nameEt.setThreshold(1);
                p_nameEt.setAdapter(adapter);
                p_nameEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String farmer = p_nameEt.getText().toString();
                        farmer_code = farmer.split(":")[0].trim();
                        DownloadProfileDetail indent = new DownloadProfileDetail();
                        indent.execute();
                    }
                });
            }
            progress.dismiss();
        }
    }

    class DownloadProfileDetail extends AsyncTask<String, Integer, Void> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ProfileUpdateActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setMax(100);
            progress.show();

            aadharNum = null;
            mobileNum = null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(String... voids) {
            MainConstant c = new MainConstant(ProfileUpdateActivity.this);
            if (c.getIp() != null) {
                try {
                    if (connect == null) {
                        flag = true;
                    } else {
                        Statement stmt = connect.createStatement();
                        try {
                            found = false;
                            String q = "select isNull(G_mobileNo,0) mobile,isnull(G_adharCardNo,0) aadhar  from vipl_accounts_master a inner join VIPL_Place_master b on a.place_id=b.place_id " +
                                    "where account_id='" + farmer_code + "' and b.place_id='" + farmer_village_code + "' and G_factoryCode = " + c.getSiteCode();
                            Log.d("myname", q);
                            ResultSet rs = stmt.executeQuery(q);
                            msg = "No record found";
                            if (rs.next()) {
                                aadharNum = rs.getString("aadhar");
                                mobileNum = rs.getString("mobile");
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
            Toast.makeText(ProfileUpdateActivity.this, msg, Toast.LENGTH_SHORT).show();
            if (found) {
                p_mobileEt.setText(mobileNum);
                p_aadharEt.setText(aadharNum);
                p_mobileEt.setEnabled(mobileNum.equals("0") || mobileNum.equals(""));
                p_aadharEt.setEnabled(aadharNum.equals("0") || aadharNum.equals(""));

            } else {
                mobileNum = null;
                aadharNum = null;

                p_mobileEt.setText("");
                p_aadharEt.setText("");

            }
            progress.dismiss();
        }
    }

    class UpdateProfileDetail extends AsyncTask<String, Integer, Void> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ProfileUpdateActivity.this);
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
        protected Void doInBackground(String... voids) {
            MainConstant c = new MainConstant(ProfileUpdateActivity.this);
            if (c.getIp() != null) {
                try {
                    if (connect == null) {
                        flag = true;
                    } else {
                        Statement stmt = connect.createStatement();
                        try {
                            found = false;
                            String q = "Update vipl_accounts_master set G_mobileNo  = " + mobileNum + ", G_adharCardNo =  " + aadharNum +
                                    " where account_id='" + farmer_code + "' and place_id='" + farmer_village_code + "' and G_factoryCode = " + c.getSiteCode();
                            Log.d("myname", q);
                            int i = stmt.executeUpdate(q);
                            found = i > 0;

                            stmt.close();
                        } catch (SQLException e) {
                            Log.d("myname", "" + e);
                            msg = e.getMessage();
                        }
                    }
                } catch (Exception e) {
                    msg = e.toString();
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

            if (found) {
                Toast.makeText(ProfileUpdateActivity.this, "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                p_mobileEt.setText("");
                p_aadharEt.setText("");
                p_nameEt.setText("");

            } else {
                Toast.makeText(ProfileUpdateActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }
    }
}
