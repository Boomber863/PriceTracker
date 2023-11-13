package com.example.pricetracker.dto.response;

import java.time.LocalDate;

public class ItemPriceResponse {

    private int id;
    private int item_id;
    private String date;
    private double price;

    public int getId() {
        return id;
    }

    public int getItemId() {
        return item_id;
    }

    public String getDateString() {
        return date;
    }

    public LocalDate getDate() {
        return LocalDate.parse(date);
    }

    public double getPrice() {
        return price;
    }
}
