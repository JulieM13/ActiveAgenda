package com.example.android.activeagenda;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


public class TagViewFragment extends Fragment {

    private List<TaskTag> allTags;
    private DBHelper dbHelper;
    private ManageTagsAdapter adapter;

    public TagViewFragment() {
        // Required empty public constructor
    }

    public static TagViewFragment newInstance() {
        return new TagViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_view, container, false);

        dbHelper = new DBHelper(getContext());
        allTags = dbHelper.getAllTags();
        adapter = new ManageTagsAdapter(getContext(), R.layout.manage_tags_item, allTags);
        ListView listView = (ListView) view.findViewById(R.id.tag_view_fragment_lv);
        listView.setAdapter(adapter);

        return view;
    }
}
