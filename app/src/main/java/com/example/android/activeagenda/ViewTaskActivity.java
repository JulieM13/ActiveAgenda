package com.example.android.activeagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

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
        taskNameTV.setText("Name: " + taskName);

        TextView taskDescriptionTV = (TextView) findViewById(R.id.view_task_task_description);
        String taskDescription = data.getString("TASK_DESCRIPTION", "");
        taskDescriptionTV.setText("Description: " + taskDescription);

        TextView taskDueDateTV = (TextView) findViewById(R.id.view_task_task_due_date);
        String taskDueDate = data.getString("TASK_DUE_DATE");
        taskDueDateTV.setText("Due Date: " + taskDueDate.toString());

        TextView taskTagNameTV = (TextView) findViewById(R.id.view_task_task_tag_name);
        System.out.println("VIEW-TASK-ACTIVITY: tag id: " + data.getLong("TAG_ID"));
        String tagName = dbHelper.getTag(data.getLong("TAG_ID")).name;
        taskTagNameTV.setText("Tag: " + tagName);
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
