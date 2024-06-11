package com.example.splanner.ui.calendar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splanner.R;
import com.example.splanner.ui.calendar.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView entriesRecyclerView;
    private EntryAdapter entryAdapter;
    private List<Entry> entryList;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = root.findViewById(R.id.calendarView);
        entriesRecyclerView = root.findViewById(R.id.entriesRecyclerView);
        Button addEntryButton = root.findViewById(R.id.addEntryButton);
        dbHelper = new DatabaseHelper(getContext());

        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entryList = new ArrayList<>();
        entryAdapter = new EntryAdapter(entryList);
        entriesRecyclerView.setAdapter(entryAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                loadEntriesForDate(selectedDate);
            }
        });

        addEntryButton.setOnClickListener(v -> startActivity(new Intent(getContext(), AddEntryActivity.class)));

        // Initial load for the current date
        String initialDate = getCurrentDate();
        loadEntriesForDate(initialDate);

        return root;
    }

    private void loadEntriesForDate(String date) {
        entryList.clear();
        Cursor cursor = dbHelper.getEntriesByDate(date);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                entryList.add(new Entry(title, description, date));
            } while (cursor.moveToNext());
        }

        cursor.close();
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
}
