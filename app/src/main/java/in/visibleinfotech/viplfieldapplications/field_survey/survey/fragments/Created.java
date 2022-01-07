package in.visibleinfotech.viplfieldapplications.field_survey.survey.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.localdatabase.PlotDatabase;
import in.visibleinfotech.viplfieldapplications.field_survey.models.Plot;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SurveyModel;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.ShowSurvey;
import in.visibleinfotech.viplfieldapplications.field_survey.survey.SurveyAdapter;

public class Created extends Fragment {
    ListView lv;
    PlotDatabase plotDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.survey_created_layout, container, false);

        lv = v.findViewById(R.id.surveyCreatedList);


        plotDatabase = new PlotDatabase(getActivity());
        ArrayList<Plot> plots = plotDatabase.getCreatedPlots();
        ArrayList<SurveyModel> surveyList = new ArrayList<>();
        for (Plot plot : plots) {
            surveyList.add(new SurveyModel(plot.getPL_GrowerCode(), plot.getPL_GName(), plot.getPL_LandName(), plot.getPL_PlotNumber(), plot.getPL_VarName(), plot.getPL_CropName(), plot.getPL_Area(), plot.getPL_PlantationDate()));
        }
        SurveyAdapter adapter = new SurveyAdapter(getActivity(), surveyList);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        return v;


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Delete created");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete created")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            View v = lv.getChildAt(position);
            TextView textView = v.findViewById(R.id.plotID3);
            String plotId = textView.getText().toString().trim();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        return super.onContextItemSelected(item);
    }
}
