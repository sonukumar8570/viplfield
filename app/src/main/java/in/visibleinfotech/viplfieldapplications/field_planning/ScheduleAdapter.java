package in.visibleinfotech.viplfieldapplications.field_planning;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.visibleinfotech.viplfieldapplications.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    Context context;
    OnCheckChangeListener onCheckChangeListener;
    private ArrayList<PlantSchedule> schedules;

    ScheduleAdapter(Context context, ArrayList<PlantSchedule> schedules) {
        this.schedules = schedules;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.planning_schedule_layoutxml, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PlantSchedule indent = schedules.get(position);
        holder.adviceNameTv.setText(indent.getP_adviceEng());
        holder.adviceCodeTv.setText(indent.getPp_adviceCode());
//        holder.statusCb.setChecked(false);

        if (indent.getPp_updStatus() == 1) {
//            holder.statusCb.setChecked(true);
            holder.linearLayout.setBackgroundResource(R.drawable.green_background);
            holder.remarkTv.setTextColor(Color.WHITE);

        }
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        if (indent.getPp_AdviceDate() != null) {

            try {
                Date inputDate = inputFormat.parse(indent.getPp_AdviceDate());
                String outputDateString = outputFormat.format(inputDate);
                holder.adviceDateTv.setText(outputDateString);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String outputDateString = "";
        if (indent.getPp_upddate() != null) {

            try {
                Date inputDate = inputFormat.parse(indent.getPp_upddate());
                outputDateString = outputFormat.format(inputDate);
                holder.adviceDateTv.setText(outputDateString);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckChangeListener = (OnCheckChangeListener) context;
                int status = indent.getPp_updStatus();
                onCheckChangeListener.onCheckChange(indent.getPp_adviceCode(), indent.getP_adviceHn(), position);
            }
        });

        if (indent.getPp_remark() != null) {
            holder.remarkTv.setText("Remark : " + indent.getPp_remark() + "\n\t\t On : " + outputDateString);
            holder.remarkTv.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }


    public interface OnCheckChangeListener {
        public void onCheckChange(String adviceCode, String adviceName, int status);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView adviceCodeTv, adviceDateTv, adviceNameTv, remarkTv;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            adviceCodeTv = itemView.findViewById(R.id.adviceCode);
            adviceDateTv = itemView.findViewById(R.id.adviceDate);
            adviceNameTv = itemView.findViewById(R.id.adviceName);
            linearLayout = itemView.findViewById(R.id.ack);
            remarkTv = itemView.findViewById(R.id.remarkTv);
//            spinner = itemView.findViewById(R.id.remarkUpdateStatus);
        }
    }


}
