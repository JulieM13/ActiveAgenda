package com.example.android.activeagenda;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
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
    private long tagIdToFilterBy = -1;


    public PlannerViewFragment() {
        // Required empty public constructor
    }

    public static PlannerViewFragment newInstance(long tagFilterId) {
        PlannerViewFragment fragment = new PlannerViewFragment();
        Bundle args = new Bundle();
        args.putLong("SELECTED_TAG_ID", tagFilterId);
        fragment.setArguments(args);
       return fragment;
    }

    public long getShownIndex() {
        return getArguments().getLong("SELECTED_TAG_ID", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Activity curActivity = getActivity();
        dbHelper = new DBHelper(curActivity.getApplicationContext());

        if (savedInstanceState != null) {
            tagIdToFilterBy = savedInstanceState.getLong("SELECTED_TAG_ID");
        }
        else if (getArguments() != null ) {
            tagIdToFilterBy = getArguments().getLong("SELECTED_TAG_ID");
        }

        System.out.println("Tag to filter by is: " + tagIdToFilterBy);

        LinearLayout layout = new LinearLayout(curActivity);
        layout.removeAllViews();
        layout.setOrientation(OrientationHelper.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));

        curDate = new Date();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDateString = formatter.format(curDate);

        Calendar c = Calendar.getInstance();

        final int DAYS_IN_PLANNER = 7;
        for( int day = 0; day < DAYS_IN_PLANNER; day++ ) {

            // Create a layout for this day
            LinearLayout dayLayout = new LinearLayout(curActivity);
            dayLayout.removeAllViews();
            dayLayout.setOrientation(OrientationHelper.VERTICAL);

            // Move to next day
            c.setTime(curDate);
            c.add(Calendar.DATE, day);
            final Date nextDate = c.getTime();

            // Set a TextView to show the date
            TextView dateTV = new TextView(curActivity);
            dateTV.setText(DateFormat.getDateInstance().format(nextDate));
            dateTV.setTextSize(25);
            dateTV.setOnClickListener(new View.OnClickListener() { //clicking date brings you to DayView
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), DayViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CUR_DATE", nextDate);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            dayLayout.addView(dateTV);
            View line = new View(curActivity);
            line.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1, 1f));
            line.setBackgroundColor(Color.DKGRAY);
            dayLayout.addView(line);

            // Get all the tasks for this day, and add a row item for each task
            allTasks = dbHelper.getAllTasks(nextDate);
            for(final Task curTask : allTasks) {

                // Only add the task if we want to see all tasks, or if this task has the desired tag
                if (tagIdToFilterBy == -1 || curTask.tagId == tagIdToFilterBy) {
                    View rowItem = inflater.inflate(R.layout.day_view_item, null);

                    CheckBox box = (CheckBox) rowItem.findViewById(R.id.dayViewItemCheckbox);
                    box.setChecked(curTask.isCompleted);
                    TextView name = (TextView) rowItem.findViewById(R.id.dayViewItemTaskName);
                    name.setText(curTask.name);
                    TextView description = (TextView) rowItem.findViewById(R.id.dayViewItemTaskDescription);
                    description.setText(curTask.description);

                    // Clicking a task will bring you to ViewTask
                    rowItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), ViewTaskActivity.class);
                            intent.putExtra("TASK_NAME", curTask.name);
                            intent.putExtra("TASK_DESCRIPTION", curTask.description);
                            intent.putExtra("TASK_DUE_DATE", curTask.dueDate);
                            intent.putExtra("TAG_ID", curTask.tagId);
                            System.out.println("DAY-VIEW-ADAPTER: tag id: " + curTask.tagId);
                            getContext().startActivity(intent);
                        }
                    });

                    ImageButton edit = (ImageButton) rowItem.findViewById(R.id.edit_task);
                    edit.setVisibility(View.INVISIBLE);

//                edit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getContext(), NewTaskActivity.class);
//                        intent.putExtra("NAME", curTask.name);
//                        intent.putExtra("DESCRIPTION", curTask.description);
//                        intent.putExtra("TAGID", curTask.tagId);
//                        intent.putExtra("DATE", curTask.dueDate);
//                        intent.putExtra("ID", curTask.id);
//                        intent.putExtra("COMPLETED", curTask.isCompleted);
//                        System.out.println("EDITING TASK: " + curTask.name);
//                        getContext().startActivity(intent);
//                    }
//                });

                    dayLayout.addView(rowItem);
                } // Close tag id filter if statement
            } // Close days for loop


            // Add blank divider between days
            TextView blank = new TextView(curActivity);
            dayLayout.addView(blank);
            layout.addView(dayLayout);
        }

        // Make the fake ListView scrollable
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.removeAllViews();;
        scrollView.addView(layout);

        // Add scrollView into LinearLayout to stop crashing? "ScrollView can host only one direct child"
        LinearLayout bigPapa = new LinearLayout(curActivity);
        bigPapa.removeAllViews();
        bigPapa.addView(scrollView);
        return bigPapa;
    }

}
