package com.example.android.activeagenda;

import android.graphics.Color;

/**
 * Created by Julie on 10/26/2016.
 */

public class TaskTag {
    public long id;
    public String name;
    public Color color;

    public TaskTag(long id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    // TODO: Add getters and setters for each attribute
}
