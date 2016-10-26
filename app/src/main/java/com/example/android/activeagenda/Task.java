package com.example.android.activeagenda;

import java.util.Date;

/**
 * Created by Julie on 10/26/2016.
 */

public class Task {
    private String taskName;
    private Date dueDate;
    private TaskTag tag;
    private String description;

    public Task(String name, Date dueDate, TaskTag tag, String description) {
        this.taskName = name;
        this.dueDate = dueDate;
        this.tag = tag;
        this.description = (description == null) ? "" : description;
    }
}
