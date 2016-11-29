package com.example.android.activeagenda;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

public class PlannerViewActivity extends MenuBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {

            // If I am the PlannerView created in Portrait mode and
            // the screen is now in Landscape mode, I am blocking the
            // split screen and should be terminated.
            finish();
            return;
        }
//        setContentView(R.layout.activity_planner_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PlannerViewFragment planner = new PlannerViewFragment();
        planner.setArguments(getIntent().getExtras());
//        System.out.println("PLANNER-VIEW-ACTIVITY: intent id: " + getIntent().getExtras().getLong("SELECTED_TAG_ID"));
//        getSupportFragmentManager().beginTransaction().replace(R.id.planner_activity_planner_view_fragment, (Fragment)planner).commit();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.planner_activity_planner_view_fragment, planner);
        transaction.add(android.R.id.content, planner);
        transaction.commit();

    }
}
