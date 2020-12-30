package com.example.todolist2;

public class Task {
    String id;
    String taskName;
    String category;
    String description;
    boolean checked;

    public Task(String id, String taskName, String category, String description, boolean checked) {
        this.id = id;
        this.taskName = taskName;
        this.category = category;
        this.description = description;
        this.checked = checked;
    }


    public Task(String taskName, boolean checked, String category) {

        this.taskName = taskName;
        this.category = category;

        this.checked = checked;
    }

    public Task() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}