package com.example.pricetracker.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

public class PriceUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Object object = intent.getSerializableExtra("items");
        if (!(object instanceof List)) {
            Log.e("DEBUG", "Provided extra must be a list of items!");
            return;
        }
        final List<ItemResponse> items = (List<ItemResponse>) object;
        if (items.isEmpty() || items.get(0) == null) {
            Log.e("DEBUG", "Not sending any notifications");
            return;
        }

        items.forEach(item -> {
            final double price = item.getNewestPrice() == null ? 0 : item.getNewestPrice().getPrice();
            NotificationSender.getInstance()
                    .sendNotification(context, "Price updated!", "The price of " + item.getName() + " has been updated to " + price + " zl");
        });
    }
}
