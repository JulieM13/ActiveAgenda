package com.example.android.activeagenda;

import android.graphics.Color;

/**
 * Created by Julie on 10/26/2016.
 */

public class TaskTag {
    public String name;
    public Color color;
//    public ArrayList<Task> tasks;

    public TaskTag(String name, Color color/*, ArrayList<Task> tasks*/) {
        this.name = name;
        this.color = color;
//        this.tasks = (tasks == null) ? new ArrayList<Task>() : tasks;
    }
}
