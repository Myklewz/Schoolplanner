package com.example.splanner.ui.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splanner.R;
import com.example.splanner.ui.todo.TodoDataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment implements TodoAdapter.OnTaskClickListener {

    private static final String TAG = "TodoFragment";
    private RecyclerView tasksRecyclerView;
    private TodoAdapter taskAdapter;
    private List<Task> taskList;
    private TodoDataSource dataSource;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        tasksRecyclerView = root.findViewById(R.id.tasksRecyclerView);
        fab = getActivity().findViewById(R.id.fab);
        dataSource = new TodoDataSource(getContext());
        dataSource.open();

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList = new ArrayList<>();
        taskAdapter = new TodoAdapter(taskList, this);
        tasksRecyclerView.setAdapter(taskAdapter);

        fab.setOnClickListener(v -> startActivity(new Intent(getContext(), AddTaskActivity.class)));

        loadTasks();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        taskList.clear();
        Cursor cursor = null;
        try {
            cursor = dataSource.getAllTasks();
            Log.d(TAG, "Loading tasks");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_DESCRIPTION));
                    taskList.add(new Task(id, title, description));
                } while (cursor.moveToNext());
            } else {
                Log.d(TAG, "No tasks found");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading tasks", e);
            Toast.makeText(getContext(), "Error loading tasks", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        taskAdapter.notifyDataSetChanged();

        if (taskList.isEmpty()) {
            Toast.makeText(getContext(), "No tasks", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskClick(int position) {
        Task task = taskList.get(position);

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dataSource.deleteTask(task.getId());
                        loadTasks();
                        Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
