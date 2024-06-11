package com.example.splanner.ui.calendar;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.splanner.R;

public class AddEntryActivity extends AppCompatActivity {

    private EditText entryTitleEditText;
    private EditText entryDescriptionEditText;
    private Button saveButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addentryactivity);

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        int secondaryColor = sharedPreferences.getInt("secondary_color", ContextCompat.getColor(this, R.color.default_secondary));

        entryTitleEditText = findViewById(R.id.entryTitleEditText);
        entryDescriptionEditText = findViewById(R.id.entryDescriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setBackgroundTintList(ColorStateList.valueOf(secondaryColor));

        saveButton.setOnClickListener(v -> {
            // Retrieve the user's input from the EditText fields
            String title = entryTitleEditText.getText().toString();
            String description = entryDescriptionEditText.getText().toString();

            // Perform the action to add the new entry to the calendar
            // This could involve saving it to a database or performing other relevant actions

            // For simplicity, we'll just finish the activity for this example
            finish();
        });
    }
}
