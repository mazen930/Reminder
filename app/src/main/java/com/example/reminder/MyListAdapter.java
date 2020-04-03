package com.example.reminder;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> reminder;
    private final ArrayList<Boolean> important;

    public MyListAdapter(Activity context, ArrayList<String> reminder, ArrayList<Boolean> important) {
        super(context, R.layout.reminders_row, reminder);

        this.context = context;
        this.reminder = reminder;
        this.important = important;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.reminders_row, null,true);

        TextView reminderText = (TextView) rowView.findViewById(R.id.reminder);
        View reputationColor = (View) rowView.findViewById(R.id.reputationColor);

        if(important.get(position))
            reputationColor.setBackgroundColor(Color.RED);
        else
            reputationColor.setBackgroundColor(Color.BLACK);
        reminderText.setText(reminder.get(position));

        return rowView;

    };
}