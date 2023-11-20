package com.example.pricetracker.api;

import com.example.pricetracker.dto.request.FollowItemRequest;
import com.example.pricetracker.dto.response.FollowedItemResponse;
import com.example.pricetracker.dto.response.ItemPriceResponse;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemService {

    @GET("/items")
    Call<List<ItemResponse>> getItems(@Header("Authorization") String token);

    @GET("/followed_items")
    Call<List<ItemResponse>> getFollowedItems(@Header("Authorization") String token);

    @GET("/item_prices")
    Call<List<ItemPriceResponse>> getItemPrices(@Header("Authorization") String token,
                                                @Query("item_id") int itemId);

    @GET("/newest_item_price")
    Call<ItemPriceResponse> getLatestItemPrice(@Header("Authorization") String token,
                                                @Query("item_id") int itemId);

    @POST("/follow")
    Call<FollowedItemResponse> followItem(@Header("Authorization") String token,
                                          @Body FollowItemRequest followItemRequest);

    @POST("/unfollow")
    Call<FollowedItemResponse> unfollowItem(@Header("Authorization") String token,
                                            @Body FollowItemRequest followItemRequest);
}
