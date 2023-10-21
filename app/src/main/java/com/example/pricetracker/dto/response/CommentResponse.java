package com.example.pricetracker.dto.response;

import java.time.LocalDate;

public class CommentResponse {

    private LocalDate created_date;
    private String text;
    private int id;
    private int item_id;
    private int user_id;

    public LocalDate getCreatedDate() {
        return created_date;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return item_id;
    }

    public int getUserId() {
        return user_id;
    }
}
