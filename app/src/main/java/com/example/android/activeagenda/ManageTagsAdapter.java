package com.example.android.activeagenda;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

            row.setTag(holder);
        }
        else {
            holder = (ManageTagsAdapter.TagHolder)row.getTag();
        }

        final TaskTag curTag = allTags.get(position);;
        holder.tagName.setText(curTag.name);
        holder.background.setBackgroundColor(curTag.color);

        if(!isDarkColor(curTag.color)){
            holder.tagName.setTextColor(Color.BLACK);
        } else{
            holder.tagName.setTextColor(Color.WHITE);
        }

        // Clicking on a Tag pulls up the PlannerView with only tasks from that tag showing
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If we're in Portrait mode, just launch a new PlannerView
                if (getContext().getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {
                    Intent intent = new Intent(getContext(), PlannerViewActivity.class);
                    intent.putExtra("SELECTED_TAG_ID", curTag.id);
                    System.out.println("MANAGE_TAGS_ADAPTER: curtagid: " + curTag.id);
                    getContext().startActivity(intent);
                }
//              else {
//                    // If we're in landscape, update the PlannerView to filter
//                    PlannerViewFragment plannerFragment = new PlannerViewFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putLong("SELECTED_TAG_ID", curTag.id);
//                    plannerFragment.setArguments(bundle);
//
//                    FragmentTransaction transaction = ((Activity)context).getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.land_tag_view_planner_fragment, plannerFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                }


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
