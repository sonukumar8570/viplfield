package in.visibleinfotech.viplfieldapplications.field_survey.zone_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.visibleinfotech.viplfieldapplications.R;

public class ZoneViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Zone> zoneArrayList;

    public ZoneViewAdapter(Context context, ArrayList<Zone> zoneArrayList) {
        this.context = context;
        this.zoneArrayList = zoneArrayList;
    }

    @Override
    public int getCount() {
        return zoneArrayList.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.survey_zone_list_layout, parent, false);
        TextView z_code_view = convertView.findViewById(R.id.z_code_view);
        TextView z_name_view = convertView.findViewById(R.id.z_name_view);
        z_code_view.setText(zoneArrayList.get(position).getZ_code());
        z_name_view.setText(zoneArrayList.get(position).getZ_name());
        return convertView;
    }
}
