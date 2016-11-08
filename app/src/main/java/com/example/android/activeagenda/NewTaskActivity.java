package com.example.android.activeagenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

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

                // TODO: add error checking - require all but descritpion and tag?
                String taskName = taskNameET.getText().toString();
                if (taskName == null || taskName == "") {
                    Toast.makeText(getApplicationContext(), "Please enter a task name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String taskDescription = taskDescriptionET.getText().toString();
                String tagName = tagSpinner.getSelectedItem().toString();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                Date date =  new Date(year, month, day);

                // TODO: actually get the tag, don't create a new one
//                TaskTag tag = new TaskTag("getActualTag", new Color());
//                Task newTask = new Task(taskName, date, tag, taskDescription, false);

//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("TASK", newTask);
//                setResult(RESULT_OK, resultIntent);
//                finish();
            }
        });
    }
}
