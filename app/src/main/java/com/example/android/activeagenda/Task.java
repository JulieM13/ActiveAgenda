package com.example.android.activeagenda;

import java.util.Date;

/**
 * Created by Julie on 10/26/2016.
 */

public class Task {
    public String name;
    public Date dueDate;
    public TaskTag tag;
    public String description;
    public boolean isCompleted;

    public Task(String name, Date dueDate, TaskTag tag, String description, boolean completed) {
        this.name = name;
        this.dueDate = dueDate;
        this.tag = tag;
        this.description = (description == null) ? "" : description;
        this.isCompleted = completed;
    }
}
