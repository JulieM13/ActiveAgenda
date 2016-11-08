package com.example.android.activeagenda;

/**
 * Created by Julie on 10/26/2016.
 */

public class Task {
    public long id;
    public String name;
    public String dueDate;
    public String description;
    public int isCompleted;
    public TaskTag tag;


    public Task(long id, String name, String dueDate, String description, int isCompleted, TaskTag tag) {
        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
        this.description = (description == null) ? "" : description;
        this.isCompleted = isCompleted;
        this.tag = tag;
    }

    // TODO: Add getters and setters for each attribute?
}
