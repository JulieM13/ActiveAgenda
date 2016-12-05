package com.example.android.activeagenda;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayViewActivity extends MenuBarActivity {
    private DBHelper dbHelper;
    private List<Task> allTasks;
    private DayViewAdapter adapter;
    public Date curDate;
    private String curDateString;
    private Format formatter;
    private TextView dateTV;

    // For notifications
    NotificationManager notificationManager;
    int notificationID = 333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(getApplicationContext());

        if (getIntent() != null ) {
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                Bundle extras = intent.getExtras();
                curDate = (Date)extras.getSerializable("CUR_DATE");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } else {
            curDate = new Date();
        }
        if (curDate == null)
            curDate = new Date();

        dateTV = (TextView) findViewById(R.id.day_view_date_tv);
        dateTV.setText(DateFormat.getDateInstance().format(curDate));

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDateString = formatter.format(curDate);

        allTasks = dbHelper.getAllTasks(curDate);
        adapter = new DayViewAdapter(this, R.layout.day_view_item, allTasks);
        ListView listView = (ListView) findViewById(R.id.day_view_lv);
        listView.setAdapter(adapter);


        FloatingActionButton createNewTaskBtn = (FloatingActionButton) findViewById(R.id.day_view_new_task_fab);
        createNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                intent.putExtra("DATE", curDateString);
                startActivityForResult(intent, 1);
            }
        });

        // For testing purposes, button to delete all tasks
        Button deleteAllTasksBtn = (Button) findViewById(R.id.day_view_delete_all_tasks_btn);
        deleteAllTasksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Deleting all Tasks from DB!");
                dbHelper.deleteAllTasksFromDB();

                allTasks = dbHelper.getAllTasks(curDate);
                adapter.updateTasks(allTasks);
            }
        });

        ImageButton prevDay = (ImageButton) findViewById(R.id.day_view_decrease_date_btn);
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.setTime(curDate);
                c.add(Calendar.DATE, -1);
                Date nextDate = c.getTime();
                updateDate(nextDate);
            }
        });

        final ImageButton nextDay = (ImageButton) findViewById(R.id.day_view_increase_date_btn);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.setTime(curDate);
                c.add(Calendar.DATE, 1);
                Date nextDate = c.getTime();
                updateDate(nextDate);
            }
        });

        showNotification(findViewById(R.id.content_day_view));
    }

    private void updateDate(Date day) {
        curDate = day;
        curDateString = formatter.format(curDate);
        dateTV.setText(DateFormat.getDateInstance().format(curDate));
        allTasks = dbHelper.getAllTasks(curDate);
        adapter.updateTasks(allTasks);
    }

    public void showNotification(View view) {
        // TODO: change the time for the demo
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 35);
        cal.set(Calendar.SECOND, 00);

        Intent alertIntent = new Intent(this, AlarmReciever.class);
        alertIntent.putExtra("CUR_DATE", curDate);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_ONE_SHOT));

        System.out.println("Alarm set");
    }


    @Override
    // We come back from the create new task dialog
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                allTasks = dbHelper.getAllTasks(curDate);
                adapter.updateTasks(allTasks);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        allTasks = dbHelper.getAllTasks(curDate);
        adapter.updateTasks(allTasks);
    }
}
