package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    EditText taskNameET;
    EditText taskDescriptionET;
    Spinner tagSpinner;
    DatePicker datePicker;

    //save info for task if editing
    long id = -1;
    boolean completed = false;

    boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        Bundle extras = getIntent().getExtras();
        Date selectedDate = new Date();
        try {
            selectedDate = (Date)formatter.parseObject((String) extras.get("DATE"));
        } catch (ParseException e){
            e.printStackTrace();
        }

        taskNameET = (EditText) findViewById(R.id.new_task_name_et);
        taskDescriptionET = (EditText) findViewById(R.id.new_task_description_et);
        tagSpinner = (Spinner) findViewById(R.id.new_task_tag_spinner);
        datePicker = (DatePicker) findViewById(R.id.new_task_datepicker);

        //Edit Task
        if(extras.getString("NAME")!=null){
            editing = true; //we have to have a name
            taskNameET.setText(extras.getString("NAME"));
            //This doesn't work vvvvv
            tagSpinner.setSelection((int)extras.getLong("TAGID"));
            id = extras.getLong("ID");
            completed = extras.getBoolean("COMPLETED");
            if(extras.getString("DESCRIPTION")!=null){
                //description is optional
                taskDescriptionET.setText(extras.getString("DESCRIPTION"));
            }
        }



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
                String taskName = taskNameET.getText().toString();
                if (taskName == null || taskName.equals("")) {
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

                TaskTag tag = (TaskTag)tagSpinner.getSelectedItem();
                long tagId = ((TaskTag)tagSpinner.getSelectedItem()).id;
                if(!editing) {
                    dbHelper.addTask(taskName, stringDate, taskDescription, NOT_COMPLETED, tagId);
                }
                else{
                    Task toUpdate = new Task(taskName, stringDate, taskDescription, completed, tagId);
                    toUpdate.setId(id);
                    dbHelper.updateTask(toUpdate);
                }

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        if(editing){
            createNewTaskBtn.setText("SAVE");
        }
    }
}
