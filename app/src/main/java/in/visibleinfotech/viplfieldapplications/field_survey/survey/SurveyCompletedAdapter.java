package in.visibleinfotech.viplfieldapplications.field_survey.survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.R;
import in.visibleinfotech.viplfieldapplications.field_survey.models.SurveyModel;

public class SurveyCompletedAdapter extends BaseAdapter {
    Context context;
    ArrayList<SurveyModel> plots;

    public SurveyCompletedAdapter(Context context, ArrayList<SurveyModel> plots) {
        this.plots = plots;
        this.context = context;
    }

    @Override
    public int getCount() {
        return plots.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.survey_comleted_adapter, parent, false);

        TextView cropTypeTv = convertView.findViewById(R.id.cropType2);
        TextView farmerName3 = convertView.findViewById(R.id.farmerName2);
        TextView plotID3 = convertView.findViewById(R.id.plotID2);
        TextView plotName3 = convertView.findViewById(R.id.plotName2);
        TextView canevariety3 = convertView.findViewById(R.id.canevariety2);
        TextView plotArea3 = convertView.findViewById(R.id.plotArea2);
        TextView plDate = convertView.findViewById(R.id.plDate);

        SurveyModel survey = plots.get(position);

        cropTypeTv.setText(survey.getCropType());
        farmerName3.setText(survey.getFarmerName());
        plotID3.setText(survey.getPlotID());
        plotName3.setText(survey.getPlotName());
        canevariety3.setText(survey.getCanevariety());
        plotArea3.setText(survey.getPlotArea());
        plDate.setText(survey.getPlDate());



        return convertView;
    }
}
