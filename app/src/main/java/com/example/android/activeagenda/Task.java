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
    public long tagId;


    public Task(String name, String dueDate, String description, boolean isCompleted, long tagId) {
        this.name = name;
        this.dueDate = dueDate;
        this.description = (description == null) ? "" : description;
        this.isCompleted = isCompleted;
        this.tagId = tagId;
    }

    public void setId(long id){
        this.id = id;
    }

    // TODO: Add getters and setters for each attribute?
}
