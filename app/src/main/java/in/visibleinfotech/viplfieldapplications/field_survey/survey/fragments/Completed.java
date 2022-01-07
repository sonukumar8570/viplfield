package in.visibleinfotech.viplfieldapplications.field_survey.survey.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import in.visibleinfotech.viplfieldapplications.field_survey.survey.SurveyCompletedAdapter;

public class Completed extends Fragment {
    ListView lv;
    TextView textView;
    PlotDatabase plotDatabase;
    ArrayList<SurveyModel> surveyList;
    ArrayList<Plot> plots;
    Button verifyBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_completed_layout, container, false);
        lv = v.findViewById(R.id.surveyListCompleted);
        textView = v.findViewById(R.id.comArTv);

        plotDatabase = new PlotDatabase(getActivity());
        plots= plotDatabase.getCompletedPlots();
        surveyList= new ArrayList<>();
        double sum = 0;

        for (Plot plot : plots) {
            surveyList.add(new SurveyModel(plot.getPL_GrowerCode(), plot.getPL_GName(), plot.getPL_LandName(), plot.getPL_PlotNumber(), plot.getPL_VarName(), plot.getPL_CropName(), plot.getPL_Area(), plot.getPL_PlantationDate()));
            sum = sum + Double.parseDouble(plot.getPL_Area());
        }
        SurveyCompletedAdapter adapter = new SurveyCompletedAdapter(getActivity(), surveyList);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

        textView.setText("Plot = " + plots.size() + ", Area = " + sum);
        verifyBtn = v.findViewById(R.id.verifyBtn);
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
        menu.add("Delete Completed");
        menu.add("Show on Map");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete Completed")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            View v = lv.getChildAt(position);
            TextView textView = v.findViewById(R.id.plotID3);
            String plotId = textView.getText().toString().trim();
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete");
            builder.setMessage("Are you surely want to delete this Plot?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    plotDatabase.deletePlot(plotId);
                    Intent i = new Intent(getActivity(), ShowSurvey.class);
                    startActivity(i);
                    getActivity().finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
        if (item.getTitle().equals("Show on Map")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            String plotId = surveyList.get(position).getPlotID();

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

            MainConstant c=new MainConstant(getActivity());
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
                        if (set.next()) {
                            plotDatabase.updateUpload(plot.getPL_PlotNumber());
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
