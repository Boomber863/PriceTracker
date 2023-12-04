package com.example.pricetracker.notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pricetracker.ItemDetailsActivity;
import com.example.pricetracker.R;
import com.example.pricetracker.api.headers.AuthorizationProvider;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.Date;
import java.util.Random;

public class NotificationSender {

    private static NotificationSender instance = null;
    private String channelId = null;

    private NotificationSender() {

    }

    public static NotificationSender getInstance() {
        if (instance == null) {
            instance = new NotificationSender();
        }
        return instance;
    }

    /**
     * @param manager notification manager
     * @param channelId id for notification channel
     */
    public void createNotificationChannel(NotificationManager manager, String channelId) {
        if(this.channelId != null) {
            Log.i("DEBUG", "Notification channel already created");
            return;
        }
        this.channelId = channelId;
        NotificationChannel channel = new NotificationChannel(channelId, "Price tracker channel", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
    }

    /**
     * @param context currently displayed activity
     * @param item item whose price has been updated
     */
    @SuppressLint("MissingPermission")
    public void sendPriceUpdateNotification(Context context, ItemResponse item) {

        if (channelId == null) {
            Log.e("DEBUG", "Notification channel does not exist - aborting");
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setContentTitle("Price updated!");
        final double price = item.getNewestPrice() == null ? 0 : item.getNewestPrice().getPrice();
        builder.setContentText("The price of " + item.getName() + " has been updated to " + price + " PLN");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSmallIcon(R.drawable.ic_followed);
        builder.setAutoCancel(true);

        Intent itemDetailsIntent = new Intent(context, ItemDetailsActivity.class);
        itemDetailsIntent.putExtra("itemName", item.getName());
        itemDetailsIntent.putExtra("itemId", item.getId());
        String authToken = AuthorizationProvider.getInstance().getAuthTokenFormatted();
        authToken = authToken.replace("Bearer ", "");
        itemDetailsIntent.putExtra("authToken", authToken);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, itemDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        final int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE) + new Random().nextInt(1000);
        manager.notify(notificationId, builder.build());
    }
}
