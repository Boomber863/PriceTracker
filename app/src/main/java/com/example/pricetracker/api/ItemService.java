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

public interface ItemService {

    @GET("/items")
    Call<List<ItemResponse>> getItems(@Header("Authorization") String token);

    @GET("/followed_items")
    Call<List<ItemResponse>> getFollowedItems(@Header("Authorization") String token);

    @GET("/item_prices")
    Call<List<ItemPriceResponse>> getItemPrices(@Header("Authorization") String token);

    @POST("/follow")
    Call<FollowedItemResponse> followItem(@Header("Authorization") String token,
                                          @Body FollowItemRequest followItemRequest);

    @POST("/unfollow")
    Call<FollowedItemResponse> unfollowItem(@Header("Authorization") String token,
                                            @Body FollowItemRequest followItemRequest);
}
