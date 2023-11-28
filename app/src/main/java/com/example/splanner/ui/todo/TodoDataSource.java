package com.example.splanner.ui.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    public long insertTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TodoDbHelper.COLUMN_TASK_NAME, task.getTaskName());
        return database.insert(TodoDbHelper.TABLE_TASKS, null, values);
    }

    public boolean deleteTask(long taskId) {
        return database.delete(TodoDbHelper.TABLE_TASKS,
                TodoDbHelper.COLUMN_ID + " = " + taskId, null) > 0;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(TodoDbHelper.TABLE_TASKS,
                null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(TodoDbHelper.COLUMN_ID));
                String taskName = cursor.getString(cursor.getColumnIndex(TodoDbHelper.COLUMN_TASK_NAME));
                tasks.add(new Task(id, taskName));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tasks;
    }
}

