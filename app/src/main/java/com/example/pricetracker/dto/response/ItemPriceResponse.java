package com.example.pricetracker.dto.response;

import java.time.LocalDate;

public class ItemPriceResponse {

    private int id;
    private int item_id;
    private LocalDate date;
    private double price;

    public int getId() {
        return id;
    }

    public int getItem_id() {
        return item_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }
}
