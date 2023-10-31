package com.example.splanner.ui.calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splanner.R;

public class AddEntryActivity extends AppCompatActivity {

    private EditText entryTitleEditText;
    private EditText entryDescriptionEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addentryactivity); // Create a corresponding layout XML file

        entryTitleEditText = findViewById(R.id.entryTitleEditText);
        entryDescriptionEditText = findViewById(R.id.entryDescriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the user's input from the EditText fields
                String title = entryTitleEditText.getText().toString();
                String description = entryDescriptionEditText.getText().toString();

                // Perform the action to add the new entry to the calendar
                // You can implement the logic to add the entry to your calendar data here
                // This could involve saving it to a database or performing other relevant actions

                // For simplicity, we'll just finish the activity for this example
                finish();
            }
        });
    }
}

