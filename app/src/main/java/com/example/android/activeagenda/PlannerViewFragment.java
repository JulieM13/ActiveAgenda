package com.example.android.activeagenda;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlannerViewFragment extends Fragment {


    public PlannerViewFragment() {
        // Required empty public constructor
    }

    public static PlannerViewFragment newInstance() {
        PlannerViewFragment fragment = new PlannerViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planner_view, container, false);
    }
}
