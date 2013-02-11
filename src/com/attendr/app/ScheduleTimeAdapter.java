package com.attendr.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.attendr.net.Utils;

public class ScheduleTimeAdapter extends ArrayAdapter<String>{
    private ArrayList<String> entries;
    private Activity activity;
 
    public ScheduleTimeAdapter(Activity a, int textViewResourceId, ArrayList<String> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView time;
        if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.schedule_hour_line_item, null);
        }
        time = (TextView) v.findViewById(R.id.time_view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)time.getLayoutParams();
		params.height = (int) TypedValue.applyDimension(
			    TypedValue.COMPLEX_UNIT_DIP, Utils.HOUR_HEIGHT, parent.getResources()
		        .getDisplayMetrics());
		time.setLayoutParams(params);
        final String s = entries.get(position);
        if (s != null) {
            time.setText(s);
        }
        return v;
    }
}