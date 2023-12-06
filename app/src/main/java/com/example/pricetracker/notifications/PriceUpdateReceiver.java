package com.example.pricetracker.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pricetracker.components.SettingsModel;
import com.example.pricetracker.dto.response.ItemResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class PriceUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

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

        items.forEach(item -> NotificationSender.getInstance().sendPriceUpdateNotification(context, item));
    }
}
