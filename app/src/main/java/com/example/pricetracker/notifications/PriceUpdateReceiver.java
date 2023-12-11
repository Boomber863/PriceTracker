package com.example.pricetracker.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pricetracker.HomepageActivity;
import com.example.pricetracker.api.headers.AuthorizationProvider;
import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.components.SettingsModel;
import com.example.pricetracker.dto.response.ItemPriceResponse;
import com.example.pricetracker.dto.response.ItemResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceUpdateReceiver extends BroadcastReceiver {

    private final List<ItemResponse> filteredItems = new ArrayList<>();
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String notificationSettingsJson = sharedPreferences.getString("settings", "");
        SettingsModel notificationSettings = gson.fromJson(notificationSettingsJson, SettingsModel.class);

        if (notificationSettings == null) {
            Log.e("DEBUG", "Cannot read notification settings - sending anyway");
        } else {
            if (!notificationSettings.areNotificationsEnabled()) {
                Log.e("DEBUG", "Notification sending is disabled - abort");
                return;
            }
        }

        String itemsJson = sharedPreferences.getString("items", "");
        List<ItemResponse> items = gson.fromJson(itemsJson, new TypeToken<List<ItemResponse>>() {
        }.getType());

        if (items == null) {
            Log.e("DEBUG", "Provided extra must be a list of items!");
            return;
        }
        if (items.isEmpty() || items.get(0) == null) {
            Log.e("DEBUG", "Not sending any notifications");
            return;
        }
        filterItemsByPrice(items);
    }

    private void filterItemsByPrice(List<ItemResponse> items) {

        if (items.isEmpty()) {
            if (filteredItems.isEmpty()) {
                return;
            }
            sendNotifications(filteredItems);
            updateItemViewModel(filteredItems);
            filteredItems.clear();
            return;
        }

        final ItemResponse item = items.get(0);
        final Call<ItemPriceResponse> latestPrice = ItemServiceProvider.getInstance().getLatestItemPrice(item.getId());

        latestPrice.enqueue(new Callback<ItemPriceResponse>() {
            @Override
            public void onResponse(Call<ItemPriceResponse> call, Response<ItemPriceResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("DEBUG", "Failed to get newest item price for " + item.getName());
                }
                final ItemPriceResponse priceResponse = response.body();
                // HERE WE ADD ONLY ITEMS WITH PRICES THAT ARE DIFFERENT THAN WHAT WE HAVE IN SHARED PREFERENCES
                if (priceResponse.getPrice() != item.getNewestPrice().getPrice()) {
                    item.setNewestPrice(priceResponse);
                    filteredItems.add(item);
                }
                items.remove(item);
                filterItemsByPrice(items);
            }

            @Override
            public void onFailure(Call<ItemPriceResponse> call, Throwable t) {
                Log.e("DEBUG", "Failed to get newest item price for " + item.getName() + " " + t.getMessage());
                items.remove(item);
                filterItemsByPrice(items);
            }
        });

    }

    private void sendNotifications(List<ItemResponse> items) {
        NotificationSender.getInstance()
                .createNotificationChannel(context.getSystemService(NotificationManager.class), "channelId");
        items.forEach(item -> NotificationSender.getInstance().sendPriceUpdateNotification(context, item));
    }

    private void updateItemViewModel(List<ItemResponse> items) {
        if (!AuthorizationProvider.getInstance().isAuthorized()) {
            return;
        }
        HomepageActivity.updateItemModel(items);
    }
}
