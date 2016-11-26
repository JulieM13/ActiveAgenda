package com.example.android.activeagenda;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PlannerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_view);

        Fragment fragment = new PlannerViewFragment();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.planner_activity_planner_view_fragment, fragment);
        fragmentTransaction.commit();

        System.out.println("Fragment has been committed");
    }
}
