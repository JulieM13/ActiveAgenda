package com.example.android.activeagenda;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;


public class TagViewFragment extends ListFragment {

    private List<TaskTag> allTags;
    private DBHelper dbHelper;
    private ManageTagsAdapter adapter;
    private long tagIdToFilterBy = -1;

    public TagViewFragment() {
        // Required empty public constructor
    }

    public static TagViewFragment newInstance() {
        return new TagViewFragment();
    }

    public boolean inLandscapeMode() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_tag_view, container, false);
//
//        dbHelper = new DBHelper(getContext());
//        allTags = dbHelper.getAllTags();
//        adapter = new ManageTagsAdapter(getContext(), R.layout.manage_tags_item, allTags);
//        ListView listView = (ListView) view.findViewById(R.id.tag_view_fragment_lv);
//        listView.setAdapter(adapter);
//
//        return view;
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbHelper = new DBHelper(getContext());
        allTags = dbHelper.getAllTags();
        adapter = new ManageTagsAdapter(getContext(), R.layout.manage_tags_item, allTags);
        setListAdapter(adapter);

        if (savedInstanceState != null) {
            tagIdToFilterBy = savedInstanceState.getLong("SELECTED_TAG_ID", -1);
        }
        if (inLandscapeMode()) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showPlanner(tagIdToFilterBy);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("SELECTED_TAG_INDEX", tagIdToFilterBy);
    }

    @Override
    public void onResume() {
        super.onResume();
        int index = (tagIdToFilterBy != -1) ? (int) tagIdToFilterBy : 1;
        getListView().setItemChecked(index, true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        showPlanner(pos);
    }

    // TODO
    private void showPlanner(long tagId) {
        tagIdToFilterBy = tagId;
        int index = (tagIdToFilterBy != -1) ? (int) tagIdToFilterBy : 1;

        if (inLandscapeMode()) {
            getListView().setItemChecked(index, true);
            PlannerViewFragment plannerFragment = (PlannerViewFragment) getFragmentManager().findFragmentById(R.id.planner_activity_planner_view_fragment);
            if( plannerFragment == null || plannerFragment.getShownIndex() != index) {
                plannerFragment = PlannerViewFragment.newInstance(tagId);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.land_tag_view_planner_fragment, plannerFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }
        else {
            Intent intent = new Intent(getActivity(), PlannerViewActivity.class);
            intent.putExtra("SELECTED_TAG_ID", tagIdToFilterBy);
            startActivity(intent);
        }
    }
}
