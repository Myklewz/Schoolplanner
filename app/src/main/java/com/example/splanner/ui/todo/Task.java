package com.example.splanner.ui.todo;
public class Task {
    private int id;
    private String taskName;

    public Task(int id, String taskName) {
        this.id = id;
        this.taskName = taskName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    @Override
    public String toString() {
        return taskName; // This will display the task's name
    }
}

