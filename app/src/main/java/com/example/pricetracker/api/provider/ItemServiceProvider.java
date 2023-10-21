package com.example.pricetracker.api.provider;

import android.util.Log;

import com.example.pricetracker.api.ItemService;
import com.example.pricetracker.api.ServerUrls;
import com.example.pricetracker.api.headers.AuthTokenException;
import com.example.pricetracker.api.headers.AuthorizationUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemServiceProvider {

    private static ItemServiceProvider instance = null;
    private final ItemService itemService;

    private ItemServiceProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUrls.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createHttpClient())
                .build();
        this.itemService = retrofit.create(ItemService.class);
    }

    public static ItemServiceProvider getInstance() {
        if(instance == null) {
            instance = new ItemServiceProvider();
        }
        return instance;
    }

    public ItemService getItemService() {
        return itemService;
    }

    private static OkHttpClient createHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            try {
                requestBuilder.header("Authorization", AuthorizationUtils.getAuthTokenFormatted());
            } catch (AuthTokenException e) {
                Log.e("AUTHORIZATION ERROR", e.getMessage());
            }
            requestBuilder.header("Content-Type", "application/json");
            return chain.proceed(requestBuilder.build());
        });
        return client;
    }
}
