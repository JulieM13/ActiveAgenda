package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DayViewActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private List<Task> allTasks;
    private DayViewAdapter adapter;
    private Date curDate;
    private String curDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(getApplicationContext());

        // TODO: get actual date from previous activity
        TextView dateTV = (TextView) findViewById(R.id.day_view_date_tv);
        curDate = new Date();
        dateTV.setText(DateFormat.getDateInstance().format(curDate));

        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        curDateString = formatter.format(curDate);


        // TODO: get actual set of tasks from this date - getAllTasks() with no parameter means get every task regardless of date,
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
                intent.putExtra("DATE", curDate);
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

                allTasks = dbHelper.getAllTasks();
                adapter = new DayViewAdapter(getApplicationContext(), R.layout.day_view_item, allTasks);
                ListView listView = (ListView) findViewById(R.id.day_view_lv);
                listView.setAdapter(adapter);
            }
        });





        // TODO: Create onClick() for decrease data and increase data buttons

    }

    @Override
    // We come back from the create new task dialog
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("In onActivityResult");
        if (requestCode == 1) {
            System.out.println("requestCode is 1");
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("resultCode is OK");
                allTasks = dbHelper.getAllTasks();
                adapter = new DayViewAdapter(this, R.layout.day_view_item, allTasks);
                ListView listView = (ListView) findViewById(R.id.day_view_lv);
                listView.setAdapter(adapter);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
