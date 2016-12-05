package com.example.android.activeagenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends MenuBarActivity {

    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(this);
        Intent intent = new Intent(this, PlannerViewActivity.class); // TODO: change this to whatever our default view is
        startActivity(intent);
    }
}
