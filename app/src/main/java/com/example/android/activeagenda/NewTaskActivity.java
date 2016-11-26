package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewTaskActivity extends MenuBarActivity {

    private DBHelper dbHelper;
    public int NO_TAG_SELECTED = -1;
    private Format formatter;
    public int NOT_COMPLETED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Bundle extras = getIntent().getExtras();
        Date selectedDate = new Date();
        try {
            selectedDate = (Date)formatter.parseObject((String) extras.get("DATE"));
        } catch (ParseException e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_new_task);
        DatePicker datePicker = (DatePicker)findViewById(R.id.new_task_datepicker);
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar);
        calendar.setTime(selectedDate);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dbHelper = new DBHelper(this);

        // Get the user's tags and display them in the spinner
        Spinner dropdown = (Spinner)findViewById(R.id.new_task_tag_spinner);
        List<TaskTag> allTags = dbHelper.getAllTags();
        for (int i = 0; i < allTags.size(); i++)
            System.out.println("In NewTaskActivity Spinner Adapter Setup: Tag name: " + allTags.get(i).name + "     Tag id: " + allTags.get(i).id);
        TagSpinnerAdapter adapter = new TagSpinnerAdapter(this, R.layout.tag_spinner_item, allTags);
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
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                String stringDate = formatter.format(date);

                // TODO: actually get tagId
                TaskTag tag = (TaskTag)tagSpinner.getSelectedItem();
                System.out.println("Grabbing selected tag from spinner in NewTaskActivity: Tag name: " + tag.name + ", Tag id: " + tag.id);
                long tagId = ((TaskTag)tagSpinner.getSelectedItem()).id;
                dbHelper.addTask(taskName, stringDate, taskDescription, NOT_COMPLETED, tagId);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
