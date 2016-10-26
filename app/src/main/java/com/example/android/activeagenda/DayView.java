package com.example.android.activeagenda;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class DayView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: get actual date from previous activity
        TextView dateTV = (TextView) findViewById(R.id.day_view_date_tv);
        String curDate = DateFormat.getDateInstance().format(new Date());
        dateTV.setText(curDate);

        // TODO: get actual set of tasks from this date
        TaskTag christmasTag = new TaskTag("Christmas Tag", new Color());
        Task t1 = new Task("Buy Christmas Presents", new Date(2016, 12, 24), christmasTag, "merry christmas", true);
        Task t2 = new Task("Open Christmas Presents", new Date(2016, 12, 25), christmasTag, "yayyy christmas!!!! I want to write a really long description to see if the description in the list view will automatically wrap to a new line or not. Do you think this is enough words??? Who knows??", false);
        Task t3 = new Task("Cry Over Christmas Presents", new Date(2016, 12, 26), christmasTag, "sad christmas", false);
        Task[] allTasks = {t1, t2, t3};
        DayViewAdapter adapter = new DayViewAdapter(this, R.layout.day_view_item, allTasks);
        ListView listView = (ListView) findViewById(R.id.day_view_lv);
        listView.setAdapter(adapter);


    }

}
