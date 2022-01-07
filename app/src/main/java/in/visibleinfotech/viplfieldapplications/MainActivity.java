package in.visibleinfotech.viplfieldapplications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaDrm;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import in.visibleinfotech.viplfieldapplications.bill_entry.BillEntry;
import in.visibleinfotech.viplfieldapplications.docs.ChoiceActivity;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.HomeActivty;
import in.visibleinfotech.viplfieldapplications.plot_end.PlotEndActivity;
import in.visibleinfotech.viplfieldapplications.profileUpdate.ProfileUpdateActivity;

public class MainActivity extends AppCompatActivity {
    int choiceId = 0;
    MainConstant mainConstant;
    TextView view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mainConstant = new MainConstant(this);
        view1 = findViewById(R.id.userNameLogin);
        view1.setText("User : " + mainConstant.geteCode() + " " + mainConstant.getUsername() + "\nCompany : " + mainConstant.getSiteCode()+" " +mainConstant.getCompanyName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckConnection().execute();
    }

    public void goNext(View view) {

        choiceId = view.getId();
        if (choiceId == R.id.survey) {
            startActivity(new Intent(MainActivity.this, HomeActivty.class));
        } else {
            new CheckLogin().execute();
        }
    }

    public void browser(View view) {
        String url = "http://www.visibleinfotech.in";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;
        String deviceId;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();
            UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
            try {
                MediaDrm wvDrm = new MediaDrm(wideVineUuid);
                byte[] wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
                deviceId = bytesToHex(wideVineId);

            } catch (Exception e) {
                // Inspect exception
                Log.d("myname", e.getMessage());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss("Saini", "vipl12345", "vipl", "43.255.152.26", null);

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "select e_active from  vipl_imei  where  E_imei1='" + deviceId + "' and E_App = 'field'";
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        String eCode = rs.getString("e_active");
                        if ((eCode.trim()).equalsIgnoreCase("yes")) {
                            flag = true;
                            msg = "Access Granted!!!";
                        } else {
                            flag = false;
                            msg = "Access Denied!!!";
                        }
                        query = "Select k_active from vipl_key where k_site_code = '" + mainConstant.getSiteCode() + "'";
                        rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            String value = rs.getString(1);
                            flag = value.equalsIgnoreCase("Yes");
                        } else {
                            flag = false;
                        }
                    } else {
                        msg = "NOT REGISTERED!!!";
                        flag = false;
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
                flag=false;
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            if (flag) {
                Intent i;

                switch (choiceId) {

                    case R.id.document:
                        i = new Intent(MainActivity.this, in.visibleinfotech.viplfieldapplications.docs.MainActivity.class);
                        startActivity(i);
                        break;
                    case R.id.schedule:
                        i = new Intent(MainActivity.this, in.visibleinfotech.viplfieldapplications.field_planning.MainActivity.class);
                        startActivity(i);
                        break;
                    case R.id.attendance:
                        i = new Intent(MainActivity.this, in.visibleinfotech.viplfieldapplications.attendance.HomeActivity.class);
                        startActivity(i);
                        break;
                    case R.id.billEntry:
                        i = new Intent(MainActivity.this, BillEntry.class);
                        startActivity(i);
                        break;
                    case R.id.profileUpdate:
                        i = new Intent(MainActivity.this, ProfileUpdateActivity.class);
                        startActivity(i);
                        break;
                    case R.id.endPlot:
                        i = new Intent(MainActivity.this, PlotEndActivity.class);
                        startActivity(i);
                        break;
                    case R.id.fieldSlip:
                        i = new Intent(MainActivity.this, in.visibleinfotech.viplfieldapplications.field_slip.MainActivity.class);
                        startActivity(i);
                        break;
                        case R.id.bricUpdate:
                        i = new Intent(MainActivity.this, in.visibleinfotech.viplfieldapplications.brics.MainActivity.class);
                        startActivity(i);
                        break;

                }
            } else {
                Toast.makeText(MainActivity.this, "Something went wrong...  Login again..", Toast.LENGTH_SHORT).show();
                logout();
            }


        }
    }

    void logout() {
        mainConstant.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }


    private class CheckConnection extends AsyncTask<Void, Void, Connection> {

        @Override
        protected Connection doInBackground(Void... voids) {
            ConnectionStr conStr = new ConnectionStr();
            Connection connect = conStr.connectionclasss(mainConstant.getDbUser(), mainConstant.getDbPass(), mainConstant.getDbName(), mainConstant.getIp(), mainConstant.getInstance());

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
