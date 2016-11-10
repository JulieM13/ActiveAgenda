package com.example.android.activeagenda;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private final Context context;
    private static String DB_PATH;

    private static final String DB_NAME="DB";
    private static final String ID_COL = "_id";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    private static final String TAGS_TABLE_NAME = "tags";
    private static final String TAGS_NAME_COL = "name";
    private static final String TAGS_COLOR_COL = "color";
    private static final String[] ALL_TAG_COLS = {
            TAGS_NAME_COL,
            TAGS_COLOR_COL };

    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String TASKS_TITLE_COL = "title";
    private static final String TASKS_DESCRIPTION_COL = "description";
    private static final String TASKS_TAGID_COL = "tagId";
    private static final String TASKS_DATE_COL = "date";
    private static final String TASKS_COMPLETED_COL = "completed";
    private static final String[] ALL_TASKS_COLS = {
            TASKS_TITLE_COL,
            TASKS_DATE_COL,
            TASKS_DESCRIPTION_COL,
            TASKS_COMPLETED_COL,
            TASKS_TAGID_COL
        };

    private static final String DB_TAGS=
            "CREATE TABLE IF NOT EXISTS " + TAGS_TABLE_NAME +
                    "(" + ID_COL + " INTEGER PRIMARY KEY NOT NULL, " +
                    TAGS_NAME_COL + " TEXT NOT NULL, " +
                    TAGS_COLOR_COL + " TEXT NOT NULL);";
    private static final String DB_TASKS=
            "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE_NAME +
                    "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    TASKS_TITLE_COL + " TEXT NOT NULL, " +
                    TASKS_DESCRIPTION_COL + " TEXT, " +
                    TASKS_TAGID_COL + " INTEGER NOT NULL, " +
                    TASKS_DATE_COL + " TEXT NOT NULL, " +
                    TASKS_COMPLETED_COL + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(tagId) REFERENCES tags(_id)\n" +
            ");";

    public DBHelper (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

        db = this.getWritableDatabase();
        db.execSQL(DB_TAGS);
        db.execSQL(DB_TASKS);
    }

    @Override
    public void onCreate (SQLiteDatabase database) { }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) { }


    // TODO: Add dbHelper.close() somewhere???

    /* TODO: Get a task based off an id */
    public Task getTask(long id) {
        //join with tags table to also return tag info
        return null;
    }

    public Task addTask(String name, String dueDate, String description, int isCompleted, long tagId) {
        ContentValues values = new ContentValues();
        values.put(TASKS_TITLE_COL, name);
        values.put(TASKS_DATE_COL, dueDate);
        values.put(TASKS_DESCRIPTION_COL, description);
        values.put(TASKS_COMPLETED_COL, isCompleted);
        values.put(TASKS_TAGID_COL, tagId);

        long insertId = db.insert(TASKS_TABLE_NAME, null, values);
        Cursor cursor = db.query(TASKS_TABLE_NAME, ALL_TASKS_COLS, ID_COL + " = " + insertId, null, null, null, null );
        cursor.moveToFirst();
        Task newTask = cursorToTask(cursor);
        cursor.close();
        System.out.println("Added a new Task! Insert ID: " + insertId + ", Name: " + name + ", Date: " + dueDate);
        return newTask;
    }

    public void deleteTask(Task task) {
        long id = task.id;
        db.delete(TASKS_TABLE_NAME, ID_COL + " = " + id, null);
        System.out.println("Deleted a Task! ID: " + id);
    }

    public void deleteAllTasksFromDB() {
        db.execSQL("delete from " + TASKS_TABLE_NAME);
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        Cursor cursor = db.query(TASKS_TABLE_NAME, ALL_TASKS_COLS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            allTasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return allTasks;
    }

    /* TODO: Get all tasks related to a date */
    public List<Task> getAllTasks(String date) {
        List<Task> allTasks = new ArrayList<>();
        Cursor cursor = db.query(TASKS_TABLE_NAME, ALL_TASKS_COLS, TASKS_DATE_COL + " = " + date, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            allTasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return allTasks;

    }

    /* TODO: Get all tasks related to a tag */
    public List<Task> getAllTasks(TaskTag tag) {
        return null;
    }

    private Task cursorToTask(Cursor cursor) {
        System.out.println("Cursor values: " + cursor.getString(0) + ", " + cursor.getString(1) + ", " +cursor.getString(2) + ", " +cursor.getInt(3) + "," + cursor.getLong(4));
        Task task = new Task(
                cursor.getString(0),        // name
                cursor.getString(1),        // date
                cursor.getString(2),        // description
                (cursor.getInt(3) != 0),    // completed
                getTag(cursor.getLong(4))); // tagId
        return task;
    }

    /* TODO: Get a tag based off an id */
    public TaskTag getTag(long id) {
        return null;
    }

    /* TODO*/
    public TaskTag addTag(long id, String name, Color color) {
        return null;
    }

    /* TODO*/
    public void deleteTag(TaskTag tag) {

    }

    /* TODO*/
    public List<TaskTag> getAllTags() {
        return null;
    }

    /* TODO*/
    public List<TaskTag> getAllTags(Color color) {
        return null;
    }

    /* TODO*/
    private TaskTag cursorToTaskTag(Cursor cursor) {
        return null;
    }

}
