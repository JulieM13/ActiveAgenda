package com.example.android.activeagenda;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            TASKS_DESCRIPTION_COL,
            TASKS_TAGID_COL,
            TASKS_DATE_COL,
            TASKS_COMPLETED_COL
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
        //DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        db = this.getWritableDatabase();
        db.execSQL(DB_TAGS);
        db.execSQL(DB_TASKS);

    }

    @Override
    public void onCreate (SQLiteDatabase database) {

    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) { }

//    // Coped from Flipped 4
//    private void copyDatabase() throws IOException {
//        InputStream input = context.getAssets().open(DB_NAME);
//        String outFileName = DB_PATH + DB_NAME;
//        OutputStream output = new FileOutputStream(outFileName);
//
//        byte[] buf = new byte[4096];
//        int len;
//        while ((len = input.read(buf)) > 0) {
//            output.write(buf, 0, len);
//        }
//        output.flush();
//        output.close();
//        input.close();
//    }
//
//    private boolean checkDatabase() {
//        SQLiteDatabase checkDB = null;
//        boolean exist = false;
//        try {
//            String path = DB_PATH + DB_NAME;
//            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
//        } catch (SQLiteException e) {
//            Log.e("DB", "Database doesn't exist");
//        }
//
//        if (checkDB != null) {
//            checkDB.close();
//            exist = true;
//        }
//        return exist;
//    }
//
//    public void createDatabase() throws IOException {
//        boolean exist = checkDatabase();
//
//        if (!exist) {
//            this.getReadableDatabase();
//            this.close();
//            try {
//                copyDatabase();
//            } catch (IOException e) {
//                throw new IOException("Fail to copy database");
//            }
//        }
//    }
//    // End of copied from Flipped 4

    // TODO: Add dbHelper.close() somewhere???

    /* TODO: Get a task based off an id */
    public Task getTask(long id) {
        //join with tags table to also return tag info
        return null;
    }

    public Task addTask(long id, String name, String dueDate, String description, int isCompleted, long tagId) {
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
        System.out.println("Added a new Task! ID: " + id + ", Name: " + name);
        return newTask;
    }

    public void deleteTask(Task task) {
        long id = task.id;
        db.delete(TASKS_TABLE_NAME, ID_COL + " = " + id, null);
        System.out.println("Deleted a Task! ID: " + id);
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
        return null;
    }

    /* TODO: Get all tasks related to a tag */
    public List<Task> getAllTasks(TaskTag tag) {
        return null;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task(      // id
                cursor.getString(1),    // name
                cursor.getString(2),    // date
                cursor.getString(3),    // description
                (cursor.getInt(4) != 0),       // completed
                getTag(cursor.getLong(5)));     // tag
        task.setId(cursor.getLong(0));
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
