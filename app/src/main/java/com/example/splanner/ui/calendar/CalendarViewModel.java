package com.example.splanner.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {

    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();

    public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }
}
