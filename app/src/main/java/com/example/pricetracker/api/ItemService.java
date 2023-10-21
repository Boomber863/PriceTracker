package com.example.pricetracker.api;

import com.example.pricetracker.dto.request.FollowItemRequest;
import com.example.pricetracker.dto.response.FollowedItemResponse;
import com.example.pricetracker.dto.response.ItemPriceResponse;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ItemService {

    @GET("/items")
    Call<List<ItemResponse>> getItems();

    @GET("/followed_items")
    Call<List<ItemResponse>> getFollowedItems();

    @GET("/item_prices")
    Call<List<ItemPriceResponse>> getItemPrices();

    @POST("/follow")
    Call<FollowedItemResponse> followItem(@Body FollowItemRequest followItemRequest);

    @POST("/unfollow")
    Call<FollowedItemResponse> unfollowItem(@Body FollowItemRequest followItemRequest);
}
