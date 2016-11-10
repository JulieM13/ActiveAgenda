package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewTaskActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    public int NO_TAG_SELECTED = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        dbHelper = new DBHelper(this);

        // TODO: get actual tags the user created
        Spinner dropdown = (Spinner)findViewById(R.id.new_task_tag_spinner);
        String[] fakeTags = new String[]{"Biology", "Algorithms", "Foreign Policy of the United States of America"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fakeTags);
        dropdown.setAdapter(adapter);

        Button createNewTaskBtn = (Button) findViewById(R.id.create_new_task_btn);
        createNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText taskNameET = (EditText) findViewById(R.id.new_task_name_et);
                EditText taskDescriptionET = (EditText) findViewById(R.id.new_task_description_et);
                Spinner tagSpinner = (Spinner) findViewById(R.id.new_task_tag_spinner);
                DatePicker datePicker = (DatePicker) findViewById(R.id.new_task_datepicker);

                String taskName = taskNameET.getText().toString();
                if (taskName == null || taskName == "") {
                    Toast.makeText(getApplicationContext(), "Please enter a task name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String taskDescription = taskDescriptionET.getText().toString();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String date = year + "-" + month + "-" + day;

                // TODO: actually get the tag, don't create a new one
                String tagName = tagSpinner.getSelectedItem().toString();
                TaskTag tag = new TaskTag("getActualTag", new Color());
                Task newTask = new Task(taskName, date.toString(), taskDescription, false, tag);

//                Intent resultIntent = new Intent();
//                setResult(RESULT_OK, resultIntent);

                dbHelper.addTask(taskName, date, taskDescription, 0, NO_TAG_SELECTED);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
