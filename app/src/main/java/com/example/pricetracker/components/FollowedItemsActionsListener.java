package com.example.pricetracker.components;

import com.example.pricetracker.dto.response.ItemResponse;

public interface FollowedItemsActionsListener {

    void onUnfollowItem(ItemResponse item);

    void goToItemDetailsActivity(ItemResponse item);
}
