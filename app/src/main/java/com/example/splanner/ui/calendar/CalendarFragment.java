package com.example.splanner.ui.calendar;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.splanner.R;
import com.example.splanner.ui.calendar.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView entriesRecyclerView;
    private EntryAdapter entryAdapter;
    private List<Entry> entryList;
    private DatabaseHelper dbHelper;
    private Button clearEntriesButton;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = root.findViewById(R.id.calendarView);
        entriesRecyclerView = root.findViewById(R.id.entriesRecyclerView);
        clearEntriesButton = root.findViewById(R.id.clearEntriesButton);
        dbHelper = new DatabaseHelper(getContext());

        sharedPreferences = getContext().getSharedPreferences("AppPreferences", getContext().MODE_PRIVATE);
        int secondaryColor = sharedPreferences.getInt("secondary_color", ContextCompat.getColor(getContext(), R.color.default_secondary));

        clearEntriesButton.setBackgroundTintList(ColorStateList.valueOf(secondaryColor));

        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entryList = new ArrayList<>();
        entryAdapter = new EntryAdapter(entryList, entry -> showDeleteDialog(entry));
        entriesRecyclerView.setAdapter(entryAdapter);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            loadEntriesForDate(selectedDate);
        });

        clearEntriesButton.setOnClickListener(v -> clearEntries());

        return root;
    }

    private void loadEntriesForDate(String date) {
        entryList.clear();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_ENTRIES + " WHERE " + DatabaseHelper.COLUMN_DATE + " = ?",
                new String[]{date}
        );

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                entryList.add(new Entry(id, title, description, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        entryAdapter.notifyDataSetChanged();

        if (entryList.isEmpty()) {
            Toast.makeText(getContext(), "No entries for this date", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEntries() {
        dbHelper.getWritableDatabase().execSQL("DELETE FROM " + DatabaseHelper.TABLE_ENTRIES);
        entryList.clear();
        entryAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "All entries cleared", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteDialog(Entry entry) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dbHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_ENTRIES,
                            DatabaseHelper.COLUMN_ID + "=?",
                            new String[]{String.valueOf(entry.getId())});
                    loadEntriesForDate(entry.getDate());
                    Toast.makeText(getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
