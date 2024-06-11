package com.example.splanner.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.splanner.R;
import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsActivity extends AppCompatActivity {

    private int selectedPrimaryColor;
    private int selectedSecondaryColor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        selectedPrimaryColor = sharedPreferences.getInt("primary_color", ContextCompat.getColor(this, R.color.default_primary));
        selectedSecondaryColor = sharedPreferences.getInt("secondary_color", ContextCompat.getColor(this, R.color.default_secondary));

        Button primaryColorButton = findViewById(R.id.btn_primary_color);
        Button secondaryColorButton = findViewById(R.id.btn_secondary_color);
        Button applyButton = findViewById(R.id.btn_apply);

        primaryColorButton.setOnClickListener(v -> showColorPickerDialog(true));
        secondaryColorButton.setOnClickListener(v -> showColorPickerDialog(false));
        applyButton.setOnClickListener(this::applyColors);
    }

    private void showColorPickerDialog(boolean isPrimary) {
        int initialColor = isPrimary ? selectedPrimaryColor : selectedSecondaryColor;
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                if (isPrimary) {
                    selectedPrimaryColor = color;
                } else {
                    selectedSecondaryColor = color;
                }
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Do nothing
            }
        });
        colorPickerDialog.show();
    }

    private void applyColors(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("primary_color", selectedPrimaryColor);
        editor.putInt("secondary_color", selectedSecondaryColor);
        editor.apply();
        recreate(); // Optionally, recreate the activity to apply the changes immediately
    }
}
