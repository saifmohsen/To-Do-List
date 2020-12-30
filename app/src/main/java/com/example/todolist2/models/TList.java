package com.example.todolist2.models;

import java.util.ArrayList;

public class TList {
    String Id;
    String Title;
    ArrayList<Task> Tasks = new ArrayList<>();

    public TList() {
    }

    public TList(String id, String title) {
        Id = id;
        Title = title;
    }

    public TList(String id, String title, ArrayList<Task> tasks) {
        Id = id;
        Title = title;
        Tasks = tasks;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public ArrayList<Task> getTasks() {
        return Tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        Tasks = tasks;
    }
}