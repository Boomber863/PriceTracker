package com.example.pricetracker.dto.request;

public class FollowItemRequest {

    private final int item_id;

    public FollowItemRequest(int itemId) {
        this.item_id = itemId;
    }

    public int getItemId() {
        return item_id;
    }
}
