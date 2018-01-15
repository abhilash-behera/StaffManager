package com.romina.staffmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.romina.staffmanager.R;


/**
 * Created by Abhilash on 08-07-2017
 */

public class NotificationDurationAdapter extends BaseAdapter {
    private String[] durations;
    private Context context;

    public NotificationDurationAdapter(Context context, String[] durations){
        this.context=context;
        this.durations=durations;
    }

    @Override
    public int getCount() {
        return durations.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.notification_duration_row_view,null);
        }

        TextView textView=(TextView)convertView.findViewById(R.id.textView);
        textView.setText(durations[position]);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return durations[position];
    }
}
