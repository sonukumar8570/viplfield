package in.visibleinfotech.viplfieldapplications.attendance;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class HistoryActivity extends AppCompatActivity {
    String slipNum;
    ListView listView;
    MainConstant c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.att_activity_history);
        listView = findViewById(R.id.listView);

    }


    @Override
    protected void onResume() {
        super.onResume();
        c = new MainConstant(this);
        slipNum = c.geteCode();

        if (!slipNum.equalsIgnoreCase("-1")) {

            new LoadDetail().execute(slipNum);
        }
    }

    class LoadDetail extends AsyncTask<String,
            Integer, ArrayList<History>> {
        ProgressDialog progress;
        boolean flag = false, found = false;
        String msg;
        String emp1Date;
        String emp1Image;
        String emp2Date;
        String emp2Image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(HistoryActivity.this);
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
        protected ArrayList<History> doInBackground(String... voids) {
            ConnectionStr conStr = new ConnectionStr();
            ArrayList<History> historyArrayList = new ArrayList<>();

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
                        String q = "select  Emp_InTIme,EMp_InTimeImage,Emp_OutTIme,EMp_OutTimeImage from vipl_empAtt where Emp_siteCode='" + c.getSiteCode() + "' and Emp_code='" + slipNum + "' order by Emp_OutTIme Desc";

                        Log.d("myname", q);
                        ResultSet rs = stmt.executeQuery(q);
                        while (rs.next()) {
                            found = true;

                            emp1Date = rs.getString("Emp_InTIme");
                            emp1Image = rs.getString("EMp_InTimeImage");
                            emp2Date = rs.getString("Emp_OutTIme");
                            emp2Image = rs.getString("EMp_OutTimeImage");
                            msg = "Record found";
                            historyArrayList.add(new History(emp1Date, emp1Image, emp2Date, emp2Image));
                        }
                    } catch (SQLException e) {
                        Log.d("myname", "" + e);
                    }
                }
            } else {
                msg = "Please Edit settings First";
            }
            return historyArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<History> v) {
            super.onPostExecute(v);
            HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this, v);
            listView.setAdapter(adapter);
            progress.dismiss();
        }
    }
}
