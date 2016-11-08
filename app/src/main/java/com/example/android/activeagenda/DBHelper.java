package com.example.android.activeagenda;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.FileNotFoundException;

public class DBHelper {
    private static final String DB_TABLES="create table tasks(_id integer primary key autoincrement, title text not null, description text, tagId integer not null, date text not null);" +
            "create table tags(_id integer primary key autoincrement, name text not null, color text not null";
    private static final String DBNAME="DB";
    private SQLiteDatabase db;
    
}
