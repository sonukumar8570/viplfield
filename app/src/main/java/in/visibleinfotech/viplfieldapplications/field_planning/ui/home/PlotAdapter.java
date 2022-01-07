package in.visibleinfotech.viplfieldapplications.field_planning.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.R;


public class PlotAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Plot> plots;

    public PlotAdapter(Context context, ArrayList<Plot> plots) {
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
        convertView = inflater.inflate(R.layout.planning_custom_plot, parent, false);
        Plot indent = plots.get(position);
        TextView landNameTv = convertView.findViewById(R.id.textView);

        landNameTv.setText(indent.getPlotId() + " " + indent.getPlotName());

        return convertView;
    }
}
