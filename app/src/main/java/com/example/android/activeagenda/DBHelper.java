package com.example.android.activeagenda;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.FileNotFoundException;

public class DBHelper {
    private static final String DB_TABLES=
            "create table tags(_id INTEGER PRIMARY KEY NOT NULL, " +
            "name TEXT NOT NULL, " +
            "color TEXT NOT NULL);"+
            "create table tasks(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "title TEXT NOT NULL, description TEXT," +
            " tagId INTEGER NOT NULL, " +
            "date TEXT NOT NULL, " +
            "deleted INTEGER NOT NULL" +
            "FOREIGN KEY(tagId) REFERENCES tags(_id)\n" +
            "););";
    private static final String DBNAME="DB";
    private SQLiteDatabase db;

}
