package com.example.splanner.ui.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splanner.R;
import com.example.splanner.ui.calendar.DatabaseHelper;

public class AddEntryActivity extends AppCompatActivity {

    private EditText entryTitleEditText;
    private EditText entryDescriptionEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addentryactivity);

        entryTitleEditText = findViewById(R.id.entryTitleEditText);
        entryDescriptionEditText = findViewById(R.id.entryDescriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        dbHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = entryTitleEditText.getText().toString();
                String description = entryDescriptionEditText.getText().toString();
                String date = getIntent().getStringExtra("selectedDate");

                if (title.isEmpty() || description.isEmpty() || date == null) {
                    Toast.makeText(AddEntryActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.addEntry(title, description, date);
                Toast.makeText(AddEntryActivity.this, "Entry added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
