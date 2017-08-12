package com.todolist.ramyashenoy.todolist.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todolist.ramyashenoy.todolist.persistence.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ramyashenoy on 8/11/17.
 */

public class ToDoListDatabaseHelper extends SQLiteOpenHelper {

    private static ToDoListDatabaseHelper sInstance;

    public static synchronized ToDoListDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ToDoListDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public static final String TABLE_TASKS = "Tasks";
    private static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DATE_CREATED = "date_created";
    public static final String COLUMN_NAME_DUE_DATE = "date_due";
    public static final String COLUMN_NAME_DETAILS = "details";
    public static final String COLUMN_NAME_PRIORITY = "priority";
    

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_TASK_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_DATE_CREATED + " DATE," +
                    COLUMN_NAME_DUE_DATE + " DATE," +
                    COLUMN_NAME_PRIORITY + " TEXT," +
                    COLUMN_NAME_DETAILS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_TASKS;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoList.db";

    private ToDoListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // Insert a task into the database
    public void addTask(Task task) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_TITLE, task.title);
            values.put(COLUMN_NAME_DATE_CREATED, new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
            values.put(COLUMN_NAME_DUE_DATE, task.dueDate != null? new SimpleDateFormat("MM/dd/yyyy").format(task.dueDate) : null);
            values.put(COLUMN_NAME_DETAILS, task.details);
            values.put(COLUMN_NAME_PRIORITY, String.valueOf(task.priority));

            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        // SELECT * FROM TASK
        String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_TASKS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    Task newTask = new Task();
                    newTask.details = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DETAILS));
                    newTask.title = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE));
                    newTask.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_ID)));
                    tasks.add(newTask);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_DETAILS, task.details);
        values.put(COLUMN_NAME_TITLE, task.title);

        return db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?",
                new String[] { String.valueOf(task.id) });
    }

    public int deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_DETAILS, task.details);
        values.put(COLUMN_NAME_TITLE, task.title);

        return db.delete(TABLE_TASKS, COLUMN_TASK_ID + "= ?", new String[] {String.valueOf(task.id)});
    }

}
