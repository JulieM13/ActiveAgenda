package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
            holder.background = (LinearLayout)row.findViewById(R.id.background);
            holder.edit = (ImageButton)row.findViewById(R.id.edit_tag);

            row.setTag(holder);
        }
        else {
            holder = (ManageTagsAdapter.TagHolder)row.getTag();
        }

        final TaskTag curTag = allTags.get(position);;
        holder.tagName.setText(curTag.name);
        holder.background.setBackground(new ColorDrawable(curTag.color));

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewTagActivity.class);
                intent.putExtra("NAME", curTag.name);
                intent.putExtra("COLOR", curTag.color);
                intent.putExtra("ID", curTag.id);
                ((Activity) getContext()).startActivityForResult(intent,1);
            }
        });

        if(!isDarkColor(curTag.color)){
            holder.tagName.setTextColor(Color.BLACK);
        } else{
            holder.tagName.setTextColor(Color.WHITE);
        }

        // Clicking on a Tag pulls up the PlannerView with only tasks from that tag showing
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlannerViewActivity.class);
                intent.putExtra("SELECTED_TAG_ID", curTag.id);
                getContext().startActivity(intent);
            }
        });

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
        LinearLayout background;
        ImageButton edit;
    }

    public void updateTags(List<TaskTag> tags){
        this.allTags = tags;
        notifyDataSetChanged();
    }

    private boolean isDarkColor(int color){
        //http://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }

}
