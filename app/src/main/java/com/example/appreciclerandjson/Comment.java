package com.example.appreciclerandjson;


public class Comment {
    private int id;
    private int postId;
    private String name;
    private String email;
    private String body;

    public Comment(int id, int postId, String name, String email, String body) {
        this.id = id;
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    // Getters and setters
}
