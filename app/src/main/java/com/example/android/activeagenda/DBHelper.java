package com.example.android.activeagenda;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
                    TAGS_COLOR_COL + " INTEGER NOT NULL);";
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
        String columns = "";
        for(String col: ALL_TASKS_COLS){
            columns += " " + col + ",";
        }
        columns = columns.substring(0, columns.length()-1);
        String query = "SELECT " + columns + " FROM " + TAGS_TABLE_NAME +
                " tags INNER JOIN " + TASKS_TABLE_NAME + " tasks ON tags." + ID_COL + "=tasks."
                + TASKS_TAGID_COL + " WHERE tasks." + ID_COL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        Task task = cursorToTask(cursor);
        cursor.close();
        return task;
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
        newTask.setId(insertId);
        cursor.close();
        System.out.println("DBHELPER: Added a new Task! Insert ID: " + insertId + ", Name: " + name + ", Date: " + dueDate + ", tagID: " + tagId);
        return newTask;
    }


    public void deleteTask(Task task) {
        long id = task.id;
        db.delete(TASKS_TABLE_NAME, ID_COL + " = " + id, null);
        System.out.println("DBHELPER: Deleted a Task! ID: " + id);
    }

    public void updateTask(Task task){
        long id= task.id;
        String name = task.name;
        String description = task.description;
        int completed = task.isCompleted? 1 : 0;
        String date = task.dueDate;
        long tagId = task.tagId;

        ContentValues values = new ContentValues();
        values.put(TASKS_TITLE_COL, name);
        values.put(TASKS_DATE_COL, date);
        values.put(TASKS_DESCRIPTION_COL, description);
        values.put(TASKS_COMPLETED_COL, completed);
        values.put(TASKS_TAGID_COL, tagId);

        String strFilter = ID_COL + "=" + id;

        int updated = db.update(TASKS_TABLE_NAME, values, strFilter, null);
        System.out.println("UPDATING TASK " + id + " with name: "+ name + " and description " + description + "did it update? = " + updated);
    }

    public void updateTag(TaskTag tag){
        ContentValues values = new ContentValues();
        values.put(TAGS_NAME_COL, tag.name);
        values.put(TAGS_COLOR_COL, tag.color);
        String strFilter = ID_COL + "=" + tag.id;

        int updated = db.update(TAGS_TABLE_NAME, values, strFilter, null);
        System.out.println("UPDATING TAG " + tag.id + " with name: "+ tag.name + " and color " + tag.color + "did it update? = " + updated);

    }

    public void deleteAllTasksFromDB() {
        db.execSQL("delete from " + TASKS_TABLE_NAME);
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        Cursor cursor = db.query(TASKS_TABLE_NAME, ALL_TASKS_COLS, null, null, null, null, TASKS_COMPLETED_COL);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            allTasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return allTasks;
    }

    public List<Task> getAllTasks(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = formatter.format(date);
        List<Task> allTasks = new ArrayList<>();
        String[] cols = Arrays.copyOf(ALL_TASKS_COLS, ALL_TASKS_COLS.length + 1);
        cols[cols.length -1] = ID_COL;
        Cursor cursor = db.query(TASKS_TABLE_NAME, cols, TASKS_DATE_COL + " = ?", new String[]{stringDate}, null, null, TASKS_COMPLETED_COL);
        cursor.moveToFirst();
        System.out.println(cursor);
        while (!cursor.isAfterLast()) {
            Task task = cursorToTaskWithId(cursor);
            allTasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return allTasks;

    }

    /* TODO: Get all tasks related to a tag */
    public List<Task> getAllTasks(TaskTag tag) {
        long tagId = tag.id;
        String columns = "";
        for(String col: ALL_TASKS_COLS){
            columns += " " + col + ",";
        }
        columns = columns.substring(0, columns.length()-1);
        String query = "SELECT " + columns + " FROM " + TAGS_TABLE_NAME +
                " tags INNER JOIN " + TASKS_TABLE_NAME + " tasks ON tags." + ID_COL + "=tasks."
                + TASKS_TAGID_COL + " WHERE tags." + ID_COL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tagId)});
        cursor.moveToFirst();
        List<Task> tasks = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }

    private Task cursorToTask(Cursor cursor) {
        System.out.println("DBHELPER: cursorToTask Cursor values: " + cursor.getString(0) + ", " + cursor.getString(1) + ", " +cursor.getString(2) + ", " +cursor.getInt(3) + "," + cursor.getLong(4));
        Task task = new Task(
                cursor.getString(0),        // name
                cursor.getString(1),        // date
                cursor.getString(2),        // description
                (cursor.getInt(3) != 0),    // completed
                cursor.getLong(4));         // tagId
        return task;
    }

    private Task cursorToTaskWithId(Cursor cursor){
        Task task = cursorToTask(cursor);
        task.setId(cursor.getLong(5));
        return task;
    }

    // TODO: Doesn't work for tag id 0?
    //is there a tag id 0?
    public TaskTag getTag(long id) {
        System.out.println("DBHELPER: getTag() id: " + id);
        Cursor cursor = db.query(TAGS_TABLE_NAME, ALL_TAG_COLS, ID_COL + " = " + id, null, null, null, null);
        System.out.println("DBHELPER: items in cursor: " + cursor.getCount());
        cursor.moveToFirst();
        TaskTag tag =  cursorToTaskTag(cursor);
        tag.setId(id);
        cursor.close();
        return tag;
    }

    public TaskTag addTag(String name, int color) {
        ContentValues values = new ContentValues();
        values.put(TAGS_NAME_COL, name);
        values.put(TAGS_COLOR_COL, color);

        long insertId = db.insert(TAGS_TABLE_NAME, null, values);
        Cursor cursor = db.query(TAGS_TABLE_NAME, ALL_TAG_COLS, ID_COL + " = " + insertId, null, null, null, null );
        cursor.moveToFirst();
        TaskTag newTag = cursorToTaskTag(cursor);
        newTag.setId(insertId);
        cursor.close();
        System.out.println("DBHELPER: Added a new Tag! Insert ID: " + insertId + ", Name: " + newTag.name + ", Tag ID: " + newTag.id);
        return newTag;
    }

    /* TODO*/
    public void deleteTag(TaskTag tag) {
        //HEY JULIE (This was supposed to go here but it was late and I was googling oops)
        /*
        * So I've been looking at this one, and a real easy way to do this can be achieved, BUT it would require remaking our DB
        * CREATE TABLE track(
        *    trackid     INTEGER,
        *    trackname   TEXT,
        *    trackartist INTEGER DEFAULT 0 REFERENCES artist(artistid) ON DELETE SET DEFAULT
        *  );
        *   in this example we can set tagids of tasks refering to a tag to 0 or whatever we want (Or null)
        *   (see ON DELETE NULL)
        *   but otherwise we'd have to do some complex stuff to get this to work. Wanted to run this by you before working on it
        * */
    }

    public List<TaskTag> getAllTags() {
        List<TaskTag> allTags = new ArrayList<>();
        String[] cols = Arrays.copyOf(ALL_TAG_COLS, ALL_TAG_COLS.length + 1);
        cols[cols.length -1] = ID_COL;
        Cursor cursor = db.query(TAGS_TABLE_NAME, cols, null, null, null, null, null);
        cursor.moveToFirst();
        int idIndex = 1;
        while (!cursor.isAfterLast()) {
            TaskTag tag = cursorToTaskTagWithId(cursor);
            tag.setId(idIndex);
            allTags.add(tag);
            cursor.moveToNext();
            idIndex++;
        }
        cursor.close();
        return allTags;
    }

    /* TODO*/
    public List<TaskTag> getAllTags(int color) {
        //HEY JULIE
        //because of how we're doing the ID's not sure we can pull this one off easily or cleanly.
        //I also am not sure why we have this call, may be forgetting something
        //but ideally no two tags would have the same color, I also can't see us having a use for this call
        return null;
    }

    public void deleteAllTagsFromDB() {
        db.execSQL("delete from " + TAGS_TABLE_NAME);

        // Add a default tag so things don't blow up
        addTag("NO TAG SELECTED", Color.rgb(0, 0, 0) );
        
    }

    private TaskTag cursorToTaskTag(Cursor cursor) {
        System.out.println("DBHELPER: cursorToTaskTag Cursor values: Tag Name: " + cursor.getString(0) + ", Tag color: " + cursor.getInt(1));
        TaskTag tag = new TaskTag(
                cursor.getString(0),        // name
                cursor.getInt(1)); // color
        return tag;
    }

    private TaskTag cursorToTaskTagWithId(Cursor cursor){
        TaskTag tag = cursorToTaskTag(cursor);
        tag.setId(cursor.getLong(2));
        return tag;
    }

}
