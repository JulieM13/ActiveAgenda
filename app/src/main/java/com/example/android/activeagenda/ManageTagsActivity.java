package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

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

        dbHelper = new DBHelper(this);

        FloatingActionButton createNewTagBtn = (FloatingActionButton) findViewById(R.id.manage_tags_new_tag_fab);
        createNewTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewTagActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        
        allTags = dbHelper.getAllTags();
        adapter = new ManageTagsAdapter(this, R.layout.manage_tags_item, allTags);
        ListView listView = (ListView) findViewById(R.id.manage_tags_lv);
        listView.setAdapter(adapter);



    }

    @Override
    // We come back from the create new tag dialog
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("In onActivityResult");
        if (requestCode == 1) {
            System.out.println("requestCode is 1");
            if (resultCode == Activity.RESULT_OK) {
                System.out.println("resultCode is OK");
                allTags = dbHelper.getAllTags();
                System.out.println("Size of all tags: " + allTags.size());
                adapter = new ManageTagsAdapter(this, R.layout.manage_tags_item, allTags);
                ListView listView = (ListView) findViewById(R.id.manage_tags_lv);
                listView.setAdapter(adapter);
            }
        }

    }

}
