package com.example.android.activeagenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Spinner dropdown = (Spinner)findViewById(R.id.new_task_tag_spinner);
        String[] fakeTags = new String[]{"Biology", "Algorithms", "Foreign Policy of the United States of America"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fakeTags);
        dropdown.setAdapter(adapter);
    }
}
