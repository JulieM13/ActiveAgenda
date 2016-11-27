package com.example.android.activeagenda;

import android.os.Bundle;

public class TagViewActivity extends MenuBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
