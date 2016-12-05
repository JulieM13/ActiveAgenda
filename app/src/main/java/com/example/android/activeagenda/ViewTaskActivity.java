package com.example.android.activeagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewTaskActivity extends MenuBarActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        Intent info = getIntent();
        Bundle data = info.getExtras();

        TextView taskNameTV = (TextView) findViewById(R.id.view_task_task_name);
        String taskName = data.getString("TASK_NAME");
        taskNameTV.setText(taskName);

        TextView taskDescriptionTV = (TextView) findViewById(R.id.view_task_task_description);
        String taskDescription = data.getString("TASK_DESCRIPTION", "");
        taskDescriptionTV.setText(taskDescription);

        TextView taskDueDateTV = (TextView) findViewById(R.id.view_task_task_due_date);
        String taskDueDate = data.getString("TASK_DUE_DATE");

        // Pretty up due date
        int year = Integer.parseInt(taskDueDate.substring(0, 4));
        int month = Integer.parseInt(taskDueDate.substring(5, 7)) -1;
        int day = Integer.parseInt(taskDueDate.substring(8, 10));  // 8, 9??
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        taskDueDateTV.setText(new SimpleDateFormat("MMM d, yyyy").format(c.getTime()));

        TextView taskTagNameTV = (TextView) findViewById(R.id.view_task_task_tag_name);
        System.out.println("VIEW-TASK-ACTIVITY: tag id: " + data.getLong("TAG_ID"));
        String tagName = dbHelper.getTag(data.getLong("TAG_ID")).name;
        taskTagNameTV.setText(tagName);

        // Share button
        Button shareTaskBtn = (Button) findViewById(R.id.view_task_share_task_btn);
        shareTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String taskName = ((TextView) findViewById(R.id.view_task_task_name)).getText().toString();
                String dueDate = ((TextView) findViewById(R.id.view_task_task_due_date)).getText().toString();
                String description = ((TextView) findViewById(R.id.view_task_task_description)).getText().toString();
                String message = "The task named " + taskName + " is due on " + dueDate + ". Don't forget! As a refresher, " +
                        "a description of the task is: " + description + "\n\nSent from ActiveAgenda";
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
}
