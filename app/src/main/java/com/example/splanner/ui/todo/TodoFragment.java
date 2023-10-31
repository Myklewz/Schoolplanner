package com.example.splanner.ui.todo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.splanner.R;

public class TodoFragment extends Fragment {

    private TodoViewModel todoViewModel;
    private TodoDataSource dataSource;
    private ArrayAdapter<Task> taskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        dataSource = new TodoDataSource(requireContext());
        dataSource.open(); // Open the database connection

        View root = inflater.inflate(R.layout.fragment_todo, container, false);
        ListView todoListView = root.findViewById(R.id.todoListView);
        EditText taskEditText = root.findViewById(R.id.editTextTask);
        Button addButton = root.findViewById(R.id.buttonAddTask);

        // Create an ArrayAdapter to display tasks in the ListView
        taskAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        todoListView.setAdapter(taskAdapter);

        addButton.setOnClickListener(v -> {
            String taskName = taskEditText.getText().toString();
            if (!taskName.isEmpty()) {
                Task task = new Task(-1, taskName); // -1 is a placeholder for the auto-generated ID
                long insertedId = dataSource.insertTask(task);
                if (insertedId != -1) {
                    task.setId((int) insertedId); // Set the actual ID from the database
                    taskAdapter.add(task);
                    taskEditText.getText().clear();
                } else {
                    Toast.makeText(requireContext(), "Failed to add task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        todoListView.setOnItemClickListener((parent, view, position, id) -> {
            Task task = taskAdapter.getItem(position);
            if (task != null) {
                boolean deleted = dataSource.deleteTask(task.getId());
                if (deleted) {
                    taskAdapter.remove(task);
                } else {
                    Toast.makeText(requireContext(), "Failed to delete task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update the taskAdapter with tasks from the database
        taskAdapter.clear();
        taskAdapter.addAll(dataSource.getAllTasks());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close(); // Close the database connection when the fragment is destroyed
    }
}
