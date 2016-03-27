package com.example.fevrec.uok;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fevrec.uok.res.Event;

import java.util.Date;
import java.util.List;

/**
 * Created by badetitou on 27/03/16.
 **/
public class EventAdapter extends ArrayAdapter<Event>{
    Context mContext;
    int layoutResourceId;
    List<Event> data = null;


    public EventAdapter(Context mContext, int layoutResourceId, List<Event> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout.
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        // object item based on the position
        Event objectItem = data.get(position);
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewDate = (TextView) convertView.findViewById(R.id.list_event_date);
        TextView textViewName = (TextView) convertView.findViewById(R.id.list_event_name);
        textViewName.setText(objectItem.getName());
        textViewDate.setText(DateUtils.getRelativeTimeSpanString(objectItem.getDate().getTime(), new Date().getTime(), DateUtils.DAY_IN_MILLIS));
        return convertView;
    }



}