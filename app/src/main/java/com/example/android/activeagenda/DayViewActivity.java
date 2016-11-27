package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
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

public class DayViewActivity extends MenuBarActivity {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}
