package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Julie on 10/26/2016.
 */

public class DayViewAdapter extends ArrayAdapter<Task> {
    Context context;
    int layoutResourceId;
    List<Task> allTasks;
    DBHelper db;

    public DayViewAdapter(Context context, int layoutResourceId, List<Task> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.allTasks = data;
        db = new DBHelper(getContext());
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
            holder.editButton = (ImageButton)row.findViewById(R.id.edit_task);
            holder.background = (RelativeLayout)row.findViewById(R.id.item);
            row.setTag(holder);
        }
        else {
            holder = (TaskHolder)row.getTag();
        }

        final Task curTask = allTasks.get(position);
        final TaskTag tag= db.getTag(curTask.tagId);

        ColorStateList colorStateList = new ColorStateList(
                new int[][] {
                        new int[] { -android.R.attr.state_checked }, // unchecked
                        new int[] {  android.R.attr.state_checked }  // checked
                },
                new int[] {
                        Color.DKGRAY,
                        tag.color
                }
        );
        holder.checkBox.setButtonTintList(colorStateList);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do stuff
                curTask.isCompleted = isChecked;
                db.updateTask(curTask);
            }
        });
        holder.checkBox.setChecked(curTask.isCompleted);
        holder.taskName.setText(curTask.name);
        holder.taskDescription.setText(curTask.description);

        // Clicking on a task will let you view the task
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewTaskActivity.class);
                intent.putExtra("TASK_NAME", curTask.name);
                intent.putExtra("TASK_DESCRIPTION", curTask.description);
                intent.putExtra("TASK_DUE_DATE", curTask.dueDate);
                intent.putExtra("TAG_ID", curTask.tagId);
                System.out.println("DAY-VIEW-ADAPTER: tag id: " + curTask.tagId);
                getContext().startActivity(intent);
            }
        });

        // Clicking on the pencil icon by a task will let you edit the task
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewTaskActivity.class);
                intent.putExtra("NAME", curTask.name);
                intent.putExtra("DESCRIPTION", curTask.description);
                intent.putExtra("TAGID", curTask.tagId);
                intent.putExtra("DATE", curTask.dueDate);
                intent.putExtra("ID", curTask.id);
                intent.putExtra("COMPLETED", curTask.isCompleted);
                System.out.println("EDITING TASK: " + curTask.name);
                ((Activity) getContext()).startActivityForResult(intent,1);
            }
        });

        return row;
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
        ImageButton editButton;
        RelativeLayout background;
    }

    public void updateTasks(List<Task> tasks){
        allTasks.clear();
        allTasks.addAll(tasks);
        this.notifyDataSetChanged();
    }


}
