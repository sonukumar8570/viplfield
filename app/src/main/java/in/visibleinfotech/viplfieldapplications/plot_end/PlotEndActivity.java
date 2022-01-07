package in.visibleinfotech.viplfieldapplications.plot_end;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;

public class PlotEndActivity extends AppCompatActivity {
    String plotId;
    EditText plotNUmberEt, plotEndRemarkEt;
    MainConstant constant;
    TextView plotDetailTv;
    Spinner plotEndSpinner;
    String remark, plotEndTypeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_end);
        plotNUmberEt = findViewById(R.id.plotIdEt);
        constant = new MainConstant(this);
        plotDetailTv = findViewById(R.id.plotDetailTv);
        plotEndRemarkEt = findViewById(R.id.plotEndRemark);
        plotEndSpinner = findViewById(R.id.plotEndSpinner);
        new DownloadPlotEndTypes().execute();
    }

    public void search(View view) {
        plotId = plotNUmberEt.getText().toString();
        if (plotId.isEmpty()) {
            Toast.makeText(this, "Plot Id is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        new DownloadIndent().execute();
    }

    public void update(View view) {
        remark = plotEndRemarkEt.getText().toString();
        plotEndTypeCode = plotEndSpinner.getSelectedItem().toString().split(":")[0].trim();
        if (plotId == null) {
            Toast.makeText(this, "Search plot detail first", Toast.LENGTH_SHORT).show();
            return;
        }
        new UpdatePlotEnd().execute();
    }

    public class DownloadIndent extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(PlotEndActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();

            plotDetailTv.setText("");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), constant.getInstance());

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "select PL_GrowerCode FarmerCode,account_name FarmerName,c.place_name FarmerVillage,d.place_name PlotVillage,PL_LandName PlotName," +
                            "e.cCropType Crop,f.Variety_Name Variety,PL_Area Area,convert(varchar(10),PL_PlantationDate,105) Date,PL_PlotEndType,PL_PlotEndBy,PL_PlotEndDate,PL_PlotEndRemark \n" +
                            "from VIPL_CanePlotSurvey a left join VIPL_Accounts_master b on a.PL_GrowerCode=b.account_id\n" +
                            "left join VIPL_Place_master c on c.place_id=a.PL_VillageCode\n" +
                            "left join VIPL_Place_master d on d.place_id=a.PL_Plotvillcode\n" +
                            "left join VIPL_CropType e on e.nCropId=a.PL_CropId\n" +
                            "left join VIPL_VarietyMaster f on f.Id=a.PL_CaneId\n" +
                            "where PL_PlotID=" + plotId;
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    flag = true;
                    msg = "No record";
                    if (rs.next()) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("Farmer : ").append(rs.getString(1)).append(" ").append(rs.getString(2));
                        builder.append("\nFarmer Village  : ").append(rs.getString(3));
                        builder.append("\nPlot Village :").append(rs.getString(4));
                        builder.append("\nPlot Name : ").append(rs.getString(5));
                        builder.append("\nCrop : ").append(rs.getString(6));
                        builder.append("\nVariety : ").append(rs.getString(7));
                        builder.append("\nArea : ").append(rs.getString(8));
                        builder.append("\nDate : ").append(rs.getString(9));
                        builder.append("\nEnd Type : ").append(rs.getString(10));
                        builder.append("\nEnd By : ").append(rs.getString(11));
                        builder.append("\nEnd Date : ").append(rs.getString(12));
                        builder.append("\nEnd Remark : ").append(rs.getString(13));
                        msg = builder.toString();

                    } else {
                        msg = "Unable to Connect to network";
                        flag = false;
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            plotDetailTv.setText(msg);
        }
    }

    public class DownloadPlotEndTypes extends AsyncTask<String, String, String> {
        String msg = "null";
        ProgressDialog progress;
        boolean flag = false;
        ArrayList<String> plotEndList;

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(PlotEndActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();
            plotEndList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), constant.getInstance());

                if (connect == null) {
                    flag = false;
                    return "not connected to internet";
                } else {
                    String query = "select Code,Name from Vipl_PlotEndMaster";
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    flag = true;
                    msg = "No record";
                    while (rs.next()) {
                        plotEndList.add(rs.getString(1) + " : " + rs.getString(2));
                    }
                }
            } catch (Exception e) {
                Log.d("myname", e.toString());
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            ArrayAdapter adapter = new ArrayAdapter(PlotEndActivity.this, R.layout.spinner_layout, R.id.spinnerTv, plotEndList);
            plotEndSpinner.setAdapter(adapter);
        }
    }

    public class UpdatePlotEnd extends AsyncTask<String, String, String> {
        ProgressDialog progress;
        boolean flag = false;

        String msg = "null";

        @Override

        protected void onPreExecute() {
            progress = new ProgressDialog(PlotEndActivity.this);
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                ConnectionStr conStr = new ConnectionStr();

                Connection connect = conStr.connectionclasss(constant.getDbUser(), constant.getDbPass(), constant.getDbName(), constant.getIp(), constant.getInstance());

                if (connect == null) {
                    flag = false;
                    msg = "not connected to internet";
                } else {
                    String query = "update VIPL_CanePlotSurvey set PL_plotEnd=1, PL_PlotEndType = " + plotEndTypeCode + ",PL_PlotEndBy='" + constant.getUsername() +
                            "', PL_PlotEndDate=getdate(), PL_PlotEndRemark ='" + remark + "' where PL_PlotID=" + plotId;
                    Log.d("myname", query);
                    Statement stmt = connect.createStatement();

                    flag = stmt.executeUpdate(query) > 0;

                }
            } catch (Exception e) {
                Log.d("##### Plot End Query: ", e.toString());
                msg = e.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String msg) {
            progress.dismiss();
            if (flag) {
                plotId = null;
                plotNUmberEt.setText("");
                plotEndRemarkEt.setText("");
                plotDetailTv.setText("");
                Toast.makeText(PlotEndActivity.this, "Detail Updated Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PlotEndActivity.this, " " + msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}