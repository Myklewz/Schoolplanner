package com.example.splanner.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Button; // Import the Button class
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.splanner.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CalendarView calendarView = binding.calendarView;
        final TextView textView = binding.textCalendar;
        Button addEntryButton = binding.addEntryButton;  // Reference to the new button

        // Handle the button click event
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add code here to open a new entry form or perform the action to add a new entry to the calendar.
                // You can show a dialog, start a new activity, or perform any other relevant action here.
                // For example, you can open a new activity for adding an entry.

                // Example:
                Intent intent = new Intent(getActivity(), AddEntryActivity.class);
                startActivity(intent);
            }
        });

        calendarViewModel.getText().observe(getViewLifecycleOwner(), text -> {
            textView.setText(text);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
