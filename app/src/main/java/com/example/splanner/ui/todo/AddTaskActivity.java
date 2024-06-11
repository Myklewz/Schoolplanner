package com.example.splanner.ui.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.splanner.R;
import com.example.splanner.ui.todo.TodoDataSource;

public class AddTaskActivity extends AppCompatActivity {

    private EditText taskTitleEditText;
    private EditText taskDescriptionEditText;
    private Button saveButton;
    private TodoDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addtaskactivity);

        taskTitleEditText = findViewById(R.id.taskTitleEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        dataSource = new TodoDataSource(this);
        dataSource.open();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitleEditText.getText().toString().trim();
                String description = taskDescriptionEditText.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataSource.addTask(title, description);
                Toast.makeText(AddTaskActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
