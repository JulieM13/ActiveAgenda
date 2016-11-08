package com.example.android.activeagenda;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
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

    private static final String DB_TABLES=
            "CREATE TABLE " + TAGS_TABLE_NAME +
                    "(" + ID_COL + " INTEGER PRIMARY KEY NOT NULL, " +
                    TAGS_NAME_COL + " TEXT NOT NULL, " +
                    TAGS_COLOR_COL + " TEXT NOT NULL);"+
            "CREATE TABLE " + TASKS_TABLE_NAME +
                    "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    TASKS_TITLE_COL + " TEXT NOT NULL, " +
                    TASKS_DESCRIPTION_COL + " TEXT, " +
                    TASKS_TAGID_COL + " INTEGER NOT NULL, " +
                    TASKS_DATE_COL + " TEXT NOT NULL, " +
                    TASKS_COMPLETED_COL + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(tagId) REFERENCES tags(_id)\n" +
            "););";

    public DBHelper (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database) {
        database.execSQL(DB_TABLES);
        db = this.getWritableDatabase();
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    // TODO: Add dbHelper.close() somewhere???

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
        return newTask;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task(
                cursor.getLong(0),      // id
                cursor.getString(1),    // name
                cursor.getString(2),    // date
                cursor.getString(3),    // description
                cursor.getInt(4),       // completed
                cursor.getLong(5));     // tagId
        return task;
    }





}
