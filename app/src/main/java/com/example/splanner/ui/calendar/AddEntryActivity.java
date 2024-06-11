package com.example.splanner.ui.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splanner.R;
import com.example.splanner.ui.calendar.DatabaseHelper;

public class AddEntryActivity extends AppCompatActivity {

    private static final String TAG = "AddEntryActivity";
    private EditText entryTitleEditText;
    private EditText entryDescriptionEditText;
    private DatePicker entryDatePicker;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addentryactivity);

        entryTitleEditText = findViewById(R.id.entryTitleEditText);
        entryDescriptionEditText = findViewById(R.id.entryDescriptionEditText);
        entryDatePicker = findViewById(R.id.entryDatePicker);
        saveButton = findViewById(R.id.saveButton);
        dbHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = entryTitleEditText.getText().toString().trim();
                String description = entryDescriptionEditText.getText().toString().trim();

                int day = entryDatePicker.getDayOfMonth();
                int month = entryDatePicker.getMonth() + 1; // Months are indexed from 0
                int year = entryDatePicker.getYear();
                String date = year + "-" + month + "-" + day;

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddEntryActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "Adding entry: Title=" + title + ", Description=" + description + ", Date=" + date);
                dbHelper.addEntry(title, description, date);
                Toast.makeText(AddEntryActivity.this, "Entry added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
