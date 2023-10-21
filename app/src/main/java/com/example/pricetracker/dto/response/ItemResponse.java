package com.example.pricetracker.dto.response;

import java.util.List;

public class ItemResponse {

    private String name;
    private int id;
    private List<CommentResponse> comments;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }
}
