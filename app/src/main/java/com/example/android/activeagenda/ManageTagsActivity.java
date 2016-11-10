package com.example.android.activeagenda;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageTagsActivity extends AppCompatActivity {
    private List<TaskTag> allTags;
    private ManageTagsAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tags);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton createNewTagBtn = (FloatingActionButton) findViewById(R.id.manage_tags_new_tag_fab);
        createNewTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Create new tag fab clicked");
            }
        });

        // TODO: actually get all the tags in the DB
//        allTags = dbHelper.getAllTags();

        TaskTag t1 = new TaskTag("Tag 1", new Color());
        TaskTag t2 = new TaskTag("who doesn't love tags?", new Color());
        allTags = new ArrayList<>(Arrays.asList(t1, t2));
        adapter = new ManageTagsAdapter(this, R.layout.manage_tags_item, allTags);
        ListView listView = (ListView) findViewById(R.id.manage_tags_lv);
        listView.setAdapter(adapter);

    }

}
