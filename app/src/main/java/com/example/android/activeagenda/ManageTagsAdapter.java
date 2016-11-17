package com.example.android.activeagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Julie on 11/10/2016.
 */

public class ManageTagsAdapter extends ArrayAdapter<TaskTag> {
    Context context;
    int layoutResourceId;
    List<TaskTag> allTags;

    public ManageTagsAdapter(Context context, int layoutResourceId, List<TaskTag> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.allTags = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ManageTagsAdapter.TagHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ManageTagsAdapter.TagHolder();
            holder.tagName = (TextView)row.findViewById(R.id.manageTagsItemTagName);
            row.setTag(holder);
        }
        else {
            holder = (ManageTagsAdapter.TagHolder)row.getTag();
        }

        TaskTag curTag = allTags.get(position);;
        holder.tagName.setText(curTag.name);

        return row;
    }

    @Override
    public int getCount() {
        return allTags.size();
    }

    @Override
    public TaskTag getItem(int pos) {
        return allTags.get(pos);
    }

    static class TagHolder {
        TextView tagName;
        // TODO: figure out how to display color here
    }

    public void updateTags(List<TaskTag> tags){
        this.allTags = tags;
        notifyDataSetChanged();
    }


}
