package com.example.splanner.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.splanner.databinding.FragmentCalendarBinding;
import com.example.splanner.ui.calendar.CalendarViewModel;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CalendarView calendarView = binding.calendarView;

        // Hier ist die TextView aus der XML-Layout-Datei
        final TextView textView = binding.textCalendar;

        calendarViewModel.getText().observe(getViewLifecycleOwner(), text -> {
            // Setze den Text in der TextView im Fragment
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
