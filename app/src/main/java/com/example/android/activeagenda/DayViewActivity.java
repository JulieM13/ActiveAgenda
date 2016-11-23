package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayViewActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private List<Task> allTasks;
    private DayViewAdapter adapter;
    private Date curDate;
    private String curDateString;
    private Format formatter;
    private TextView dateTV;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(getApplicationContext());

        // TODO: get actual date from previous activity
        dateTV = (TextView) findViewById(R.id.day_view_date_tv);
        curDate = new Date();
        dateTV.setText(DateFormat.getDateInstance().format(curDate));

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDateString = formatter.format(curDate);

        System.out.println("The current date is: " + curDateString);
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

        Button prevDay = (Button) findViewById(R.id.day_view_decrease_date_btn);
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

        final Button nextDay = (Button) findViewById(R.id.day_view_increase_date_btn);
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


        // TODO: Create onClick() for decrease data and increase data buttons

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void updateDate(Date day) {
        curDate = day;
        curDateString = formatter.format(curDate);
        dateTV.setText(DateFormat.getDateInstance().format(curDate));
        allTasks = dbHelper.getAllTasks(curDate);
        adapter.updateTasks(allTasks);
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
    /* Create the overflow menu */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    /* Set click actions of overflow menu options */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_manage_tags:
                Intent intent = new Intent(this, ManageTagsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_export_tasks_to_phone_cal:
                exportAllTasksToCalendar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportAllTasksToCalendar() {
        for (int i = 0; i < allTasks.size(); i++) {
            Task curTask = allTasks.get(i);
            Intent calIntent = new Intent(Intent.ACTION_EDIT)
                .setType("vnd.android.cursor.item/event")
                .putExtra(CalendarContract.Events.TITLE, curTask.name)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, curTask.dueDate)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, curTask.dueDate)
                .putExtra(CalendarContract.Events.ALL_DAY, false)
                .putExtra(CalendarContract.Events.ALL_DAY, curTask.description);
            startActivity(calIntent);
        }
    }
}
