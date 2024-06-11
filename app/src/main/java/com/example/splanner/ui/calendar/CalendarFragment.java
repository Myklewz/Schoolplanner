package com.example.splanner.ui.calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splanner.R;
import com.example.splanner.ui.calendar.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment implements EntryAdapter.OnEntryClickListener {

    private static final String TAG = "CalendarFragment";
    private CalendarView calendarView;
    private RecyclerView entriesRecyclerView;
    private EntryAdapter entryAdapter;
    private List<Entry> entryList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = root.findViewById(R.id.calendarView);
        entriesRecyclerView = root.findViewById(R.id.entriesRecyclerView);
        fab = getActivity().findViewById(R.id.fab); // Access the FloatingActionButton from the activity layout
        dbHelper = new DatabaseHelper(getContext());

        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entryList = new ArrayList<>();
        entryAdapter = new EntryAdapter(entryList, this);
        entriesRecyclerView.setAdapter(entryAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                loadEntriesForDate(selectedDate);
            }
        });

        fab.setOnClickListener(v -> startActivity(new Intent(getContext(), AddEntryActivity.class)));

        // Initial load for the current date
        String initialDate = getCurrentDate();
        loadEntriesForDate(initialDate);

        return root;
    }

    private void loadEntriesForDate(String date) {
        entryList.clear();
        Cursor cursor = null;
        try {
            cursor = dbHelper.getEntriesByDate(date);
            Log.d(TAG, "Loading entries for date: " + date);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                    entryList.add(new Entry(id, title, description, date));
                } while (cursor.moveToNext());
            } else {
                Log.d(TAG, "No entries found for date: " + date);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading entries", e);
            Toast.makeText(getContext(), "Error loading entries", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        entryAdapter.notifyDataSetChanged();

        if (entryList.isEmpty()) {
            Toast.makeText(getContext(), "No entries for this date", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are indexed from 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    @Override
    public void onEntryClick(int position) {
        Entry entry = entryList.get(position);

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteEntry(entry.getId());
                        loadEntriesForDate(entry.getDate());
                        Toast.makeText(getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void clearAllEntries() {
        new AlertDialog.Builder(getContext())
                .setTitle("Clear All Entries")
                .setMessage("Are you sure you want to delete all entries?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteAllEntries();
                        entryList.clear();
                        entryAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "All entries deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
