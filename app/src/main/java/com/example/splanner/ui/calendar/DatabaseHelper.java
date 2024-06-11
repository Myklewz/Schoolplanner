package com.example.splanner.ui.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "calendar.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ENTRIES = "entries";
    public static final String COLUMN_ID = "id"; // Update to the correct column name
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ENTRIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_DATE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

    public Cursor getEntriesByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG, "Querying entries for date: " + date);
        return db.rawQuery("SELECT * FROM " + TABLE_ENTRIES + " WHERE " + COLUMN_DATE + " = ?", new String[]{date});
    }

    public void addEntry(String title, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        long id = db.insert(TABLE_ENTRIES, null, values);
        Log.d(TAG, "Inserted entry with ID: " + id);
    }

    public void deleteEntry(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_ENTRIES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "Deleted " + rowsDeleted + " row(s) with ID: " + id);
    }

    public void deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_ENTRIES, null, null);
        Log.d(TAG, "Deleted " + rowsDeleted + " row(s) from table " + TABLE_ENTRIES);
    }
}
