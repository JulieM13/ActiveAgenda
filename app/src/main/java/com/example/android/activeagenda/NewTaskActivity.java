package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                int month = datePicker.getMonth();
                int year = datePicker.getYear() - 1900; //apparently this is a thing http://stackoverflow.com/questions/17985467/datepicker-dialog-picks-wrong-year
                Date date = new Date(year, month, day);
                System.out.println("year: " + year + " month: " + month + " day: " + day); // HEY RACHAEL for today: year: 2016 month: 11 day: 9
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                String stringDate = formatter.format(date);

                // TODO: actually get tagId
                int tagId = NO_TAG_SELECTED;
                dbHelper.addTask(taskName, stringDate, taskDescription, 0, tagId);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
