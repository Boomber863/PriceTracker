package com.example.pricetracker.api.provider;

import com.example.pricetracker.api.ItemService;
import com.example.pricetracker.api.ServerUrls;
import com.example.pricetracker.api.headers.AuthTokenException;
import com.example.pricetracker.api.headers.AuthorizationUtils;
import com.example.pricetracker.dto.request.FollowItemRequest;
import com.example.pricetracker.dto.response.FollowedItemResponse;
import com.example.pricetracker.dto.response.ItemPriceResponse;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemServiceProvider {

    private static ItemServiceProvider instance = null;
    private final ItemService itemService;
    private final String authToken;

    private ItemServiceProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUrls.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.itemService = retrofit.create(ItemService.class);
        try {
            this.authToken = AuthorizationUtils.getAuthTokenFormatted();
        } catch (AuthTokenException e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemServiceProvider getInstance() {
        if(instance == null) {
            instance = new ItemServiceProvider();
        }
        return instance;
    }

    public Call<List<ItemResponse>> getItems() {
        return itemService.getItems(authToken);
    }


    public Call<List<ItemResponse>> getFollowedItems() {
        return itemService.getFollowedItems(authToken);
    }

    public Call<List<ItemPriceResponse>> getItemPrices(int itemId) {
        return itemService.getItemPrices(authToken, itemId);
    }

    public Call<FollowedItemResponse> followItem(FollowItemRequest followItemRequest) {
        return itemService.followItem(authToken, followItemRequest);
    }

    public Call<FollowedItemResponse> unfollowItem(FollowItemRequest followItemRequest) {
        return itemService.unfollowItem(authToken, followItemRequest);
    }
}
