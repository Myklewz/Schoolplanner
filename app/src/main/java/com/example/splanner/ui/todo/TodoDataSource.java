package com.example.splanner.ui.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TodoDataSource {

    private SQLiteDatabase database;
    private TodoDbHelper dbHelper;

    public TodoDataSource(Context context) {
        dbHelper = new TodoDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTask(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(TodoDbHelper.COLUMN_TITLE, title);
        values.put(TodoDbHelper.COLUMN_DESCRIPTION, description);
        database.insert(TodoDbHelper.TABLE_TASKS, null, values);
    }

    public void deleteTask(long taskId) {
        database.delete(TodoDbHelper.TABLE_TASKS, TodoDbHelper.COLUMN_ID + " = " + taskId, null);
    }

    public Cursor getAllTasks() {
        return database.query(TodoDbHelper.TABLE_TASKS,
                new String[]{TodoDbHelper.COLUMN_ID, TodoDbHelper.COLUMN_TITLE, TodoDbHelper.COLUMN_DESCRIPTION},
                null, null, null, null, null);
    }
}
