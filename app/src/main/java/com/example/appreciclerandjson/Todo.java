package com.example.appreciclerandjson;

public class Todo {
    private int id;
    private int userId;
    private String title;
    private boolean completed;

    public Todo(int id, int userId, String title, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.completed = completed;
    }

    // Getters and setters
}
