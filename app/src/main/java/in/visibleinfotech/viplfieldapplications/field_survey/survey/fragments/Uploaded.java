package in.visibleinfotech.viplfieldapplications.field_survey.survey.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.ConnectionStr;
import in.visibleinfotech.viplfieldapplications.MainConstant;
import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.PlotDatabase;
import in.visibleinfotech.viplfieldapplications.field_survey.models.Plot;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SurveyModel;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.ShowSurvey;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.SurveyAdapter;

public class Uploaded extends Fragment {
    ListView lv;
    PlotDatabase plotDatabase;
    TextView textView;
    Button verifyBtn;
    ArrayList<Plot> plots;
    MainConstant c;
    ArrayList<SurveyModel> surveyList2;
    double sum = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_upload_layout, container, false);
        lv = v.findViewById(R.id.surveyListUploaded);
        textView = v.findViewById(R.id.uploArTv);
        verifyBtn = v.findViewById(R.id.verifyBtn);
        c = new MainConstant(getActivity());

        plotDatabase = new PlotDatabase(getActivity());
        plots = plotDatabase.getUploadedPlots();
        surveyList2 = new ArrayList<>();

        for (Plot plot : plots) {
            surveyList2.add(new SurveyModel(plot.getPL_GrowerCode(), plot.getPL_GName(), plot.getPL_LandName(), plot.getPL_PlotNumber(), plot.getPL_VarName(), plot.getPL_CropName(), plot.getPL_Area(), plot.getPL_PlantationDate()));
            sum = sum + Double.parseDouble(plot.getPL_Area());
        }
        SurveyAdapter adapter = new SurveyAdapter(getActivity(), surveyList2);
        lv.setAdapter(adapter);

        textView.setText("Plot = " + plots.size() + ", Area = " + sum);
        registerForContextMenu(lv);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Verify().execute();
            }
        });
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Move to Completed");
        menu.add("Show Map");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Move to Completed")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            String plotId = surveyList2.get(position).getPlotID();
            plotDatabase.updateCompleted(plotId);
            Toast.makeText(getActivity(), "PLOT moved to completed", Toast.LENGTH_SHORT).show();
            surveyList2 = new ArrayList<>();
            sum = 0;
            for (Plot plot : plots) {
                surveyList2.add(new SurveyModel(plot.getPL_GrowerCode(), plot.getPL_GName(), plot.getPL_LandName(), plot.getPL_PlotNumber(), plot.getPL_VarName(), plot.getPL_CropName(), plot.getPL_Area(), plot.getPL_PlantationDate()));
                sum = sum + Double.parseDouble(plot.getPL_Area());
            }
            SurveyAdapter adapter = new SurveyAdapter(getActivity(), surveyList2);
            lv.setAdapter(adapter);
            textView.setText("Plot = " + plots.size() + ", Area = " + sum);

        }
        if (item.getTitle().equals("Show Map")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            String plotId = surveyList2.get(position).getPlotID();

            Intent i = new Intent(getActivity(), ShowMapActivity.class);
            i.putExtra("landName", plotId);
            startActivity(i);
        }
        return super.onContextItemSelected(item);
    }

    public class Verify extends AsyncTask<String, String, Integer> {
        ProgressDialog progress;
        boolean flag = false;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading..please wait.....");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected void onPostExecute(Integer result) {

            Toast.makeText(getActivity(), "total not verified = " + result, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), ShowSurvey.class);
            getActivity().startActivity(i);
            getActivity().finish();
        }


        @Override
        protected Integer doInBackground(String... params) {
            int total = 0;
            ConnectionStr conStr = new ConnectionStr();


            Connection connect = conStr.connectionclasss(c.getDbUser(), c.getDbPass(), c.getDbName(), c.getIp(), c.getInstance());
            if (connect == null) {
                flag = true;
            } else {
                for (Plot plot : plots) {
                    String q = "Select * from VIPL_CanePlotSurvey where PL_PlotNumber = '" + plot.getPL_PlotNumber() + "'";
                    try {
                        Statement stmt = connect.createStatement();
                        Log.d("myname", q);
                        ResultSet set = stmt.executeQuery(q);
                        if (!set.next()) {
                            plotDatabase.updateCompleted(plot.getPL_PlotNumber());
                            total++;
                        }
                        stmt.close();
                    } catch (SQLException e1) {
                        Log.d("myname", e1.getMessage());
                    }
                }
            }
            // Toast.makeText(MainActivity.this, "Data Exported Successfully", Toast.LENGTH_SHORT).show();
            return total;
        }
    }
}

