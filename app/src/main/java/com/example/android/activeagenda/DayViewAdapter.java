package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Julie on 10/26/2016.
 */

public class DayViewAdapter extends ArrayAdapter<Task> {
    Context context;
    int layoutResourceId;
    List<Task> allTasks;

    public DayViewAdapter(Context context, int layoutResourceId, List<Task> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.allTasks = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TaskHolder();
            holder.checkBox = (CheckBox)row.findViewById(R.id.dayViewItemCheckbox);
            holder.taskName = (TextView)row.findViewById(R.id.dayViewItemTaskName);
            holder.taskDescription = (TextView)row.findViewById(R.id.dayViewItemTaskDescription);
            row.setTag(holder);
        }
        else {
            holder = (TaskHolder)row.getTag();
        }

        Task curTask = allTasks.get(position);
        holder.checkBox.setChecked(curTask.isCompleted);
        holder.taskName.setText(curTask.name);
        holder.taskDescription.setText(curTask.description);

        return row;
    }

    @Override
    public int getCount() {
        return allTasks.size();
    }

    @Override
    public Task getItem(int pos) {
        return allTasks.get(pos);
    }

    static class TaskHolder {
        CheckBox checkBox;
        TextView taskName;
        TextView taskDescription;
    }


}
