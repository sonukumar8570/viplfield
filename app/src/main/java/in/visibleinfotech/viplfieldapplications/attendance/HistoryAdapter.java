package in.visibleinfotech.viplfieldapplications.attendance;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.visibleinfotech.viplfieldapplications.R;

public class HistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<History> historyArrayList;

    public HistoryAdapter(Context context, ArrayList<History> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @Override
    public int getCount() {
        return historyArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.att_record_list, viewGroup, false);
        TextView textView1 = view.findViewById(R.id.tv1);
        TextView textView2 = view.findViewById(R.id.tv2);

        History history = historyArrayList.get(i);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String outputDateStr = null, outputDateStr2 = null;
        try {
            Date date1 = inputFormat.parse(history.getTime1());
            outputDateStr = outputFormat.format(date1);
            if (history.getTime2() != null) {
                Date date2 = inputFormat.parse(history.getTime2());
                outputDateStr2 = outputFormat.format(date2);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (history.getTime1() != null) textView1.setText(outputDateStr);
        if (history.getTime2() != null) textView2.setText(outputDateStr2);

        return view;
    }

    private void convertByteArray(String encodedImage, ImageView imageView) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Glide.with(context)
                .asBitmap()
                .load(decodedString)
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(imageView);
    }

}
