package com.example.android.activeagenda;

/**
 * Created by Julie on 10/26/2016.
 */

public class TaskTag {
    public long id;
    public String name;
    public int color;

    public TaskTag(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public void setId(long id){
        this.id = id;
    }
    // TODO: Add getters and setters for each attribute
}
