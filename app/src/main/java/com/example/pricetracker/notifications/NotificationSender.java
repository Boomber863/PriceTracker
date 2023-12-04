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

import com.example.pricetracker.LoginActivity;
import com.example.pricetracker.R;

import java.util.Date;

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
     * @param title title of the notification
     * @param text content of the notification
     */
    @SuppressLint("MissingPermission")
    public void sendNotification(Context context, String title, String text) {

        if (channelId == null) {
            Log.e("DEBUG", "Notification channel does not exist - aborting");
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSmallIcon(R.drawable.ic_followed);
        builder.setAutoCancel(true);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, new Intent(context, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        final int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        manager.notify(notificationId, builder.build());
    }
}
