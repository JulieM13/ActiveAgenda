package com.example.android.activeagenda;

import java.util.Date;

/**
 * Created by Julie on 10/26/2016.
 */

public class Day {

    private Task task;
    private Date date;

    public Day(Date date, Task task) {
        this.task = task;
        this.date = date;
    }
}
