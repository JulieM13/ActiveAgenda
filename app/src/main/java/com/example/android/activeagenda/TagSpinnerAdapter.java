package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Julie on 11/11/2016.
 */

public class TagSpinnerAdapter extends ArrayAdapter<TaskTag> {
    Context context;
    int layoutResourceId;
    List<TaskTag> allTags;

    public TagSpinnerAdapter(Context context, int layoutResourceId, List<TaskTag> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.allTags = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TagSpinnerAdapter.TagHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TagSpinnerAdapter.TagHolder();
            holder.taskName = (TextView)row.findViewById(R.id.tagSpinnerItemTagName);
            row.setTag(holder);
        }
        else {
            holder = (TagSpinnerAdapter.TagHolder)row.getTag();
        }

        TaskTag curTag = allTags.get(position);
        System.out.println("Tag id in spinner: " + curTag.id);
        holder.taskName.setText(curTag.name);

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView tagName = new TextView(context);
        tagName.setTextSize((float)24);
        tagName.setTextColor(Color.BLACK);
        tagName.setText(allTags.get(position).name);

        return tagName;
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
        TextView taskName;
        // TODO: add a color thingy?
        // I think spinners might just want a TextView, based on errors I've been getting
    }
}
