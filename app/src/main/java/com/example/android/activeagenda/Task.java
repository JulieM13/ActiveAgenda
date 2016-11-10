package com.example.android.activeagenda;

/**
 * Created by Julie on 10/26/2016.
 */

public class Task {
    public long id;
    public String name;
    public String dueDate;
    public String description;
    public boolean isCompleted;
    public TaskTag tag;


    public Task(String name, String dueDate, String description, boolean isCompleted, TaskTag tag) {
        this.name = name;
        this.dueDate = dueDate;
        this.description = (description == null) ? "" : description;
        this.isCompleted = isCompleted;
        this.tag = tag;
    }

    public void setId(long id){
        this.id = id;
    }

    // TODO: Add getters and setters for each attribute?
}
