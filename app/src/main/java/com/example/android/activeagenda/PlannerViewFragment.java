package com.example.android.activeagenda;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlannerViewFragment extends Fragment {
    private DBHelper dbHelper;
    private List<Task> allTasks;
    private Date curDate;
    private String curDateString;
    private Format formatter;


    public PlannerViewFragment() {
        // Required empty public constructor
    }

    public static PlannerViewFragment newInstance() {
       return new PlannerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Activity curActivity = getActivity();
        dbHelper = new DBHelper(curActivity.getApplicationContext());

        LinearLayout layout = new LinearLayout(curActivity);
        layout.removeAllViews();
        layout.setOrientation(OrientationHelper.VERTICAL);

        curDate = new Date();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDateString = formatter.format(curDate);

        Calendar c = Calendar.getInstance();

        final int DAYS_IN_PLANNER = 7;
        for( int day = 0; day < DAYS_IN_PLANNER; day++ ) {

            LinearLayout dayLayout = new LinearLayout(curActivity);
            dayLayout.removeAllViews();
            dayLayout.setOrientation(OrientationHelper.VERTICAL);

            // Move to next day
            c.setTime(curDate);
            c.add(Calendar.DATE, day);
            Date nextDate = c.getTime();

            // Set a TextView to show a date
            TextView dateTV = new TextView(curActivity);
            dateTV.setText(DateFormat.getDateInstance().format(nextDate));
            dayLayout.addView(dateTV);

            // Get all the tasks for this day, and add a row item for each task
            allTasks = dbHelper.getAllTasks(nextDate);
            for(Task curTask : allTasks) {
                View rowItem = inflater.inflate(R.layout.day_view_item, null);

                CheckBox box = (CheckBox)rowItem.findViewById(R.id.dayViewItemCheckbox);
                box.setChecked(curTask.isCompleted);
                TextView name = (TextView)rowItem.findViewById(R.id.dayViewItemTaskName);
                name.setText(curTask.name);
                TextView description = (TextView)rowItem.findViewById(R.id.dayViewItemTaskDescription);
                description.setText(curTask.description);
                dayLayout.addView(rowItem);
            }

            // Add blank divider between days
            TextView blank = new TextView(curActivity);
            dayLayout.addView(blank);
            layout.addView(dayLayout);
        }

        return layout;
    }

}
