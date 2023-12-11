package com.example.pricetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.components.ItemViewModel;
import com.example.pricetracker.components.SettingsModel;
import com.example.pricetracker.dto.response.ItemResponse;
import com.example.pricetracker.fragments.FragmentAdapter;
import com.example.pricetracker.notifications.NotificationSender;
import com.example.pricetracker.notifications.PriceUpdateReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity {

    private static final int ACTION_LIST = R.id.action_list;
    private static final int ACTION_FOLLOWED = R.id.action_followed;
    private static final int ACTION_SETTINGS = R.id.action_settings;

    private static ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentAdapter(this));

        // Dodaj Bottom Navigation View Listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.setSelectedItemId(getSelectedItemId(position));
            }
        });

        itemViewModel.getFollowedItemsLiveData()
                .observe(this, this::updateSharedPreferencesItems);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            viewPager.setCurrentItem(getSelectedItemPosition(itemId), true);
            return true;
        });
        getAvailableItems();
        prepareNotificationSystem();
    }

    private void updateSharedPreferencesItems(List<ItemResponse> followedItems) {
        SharedPreferences sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("items", new Gson().toJson(new ArrayList<>(followedItems)));
        editor.apply();
    }

    private void prepareNotificationSystem() {

        // THIS CREATES NOTIFICATION CHANNEL
        NotificationSender.getInstance()
                .createNotificationChannel(getSystemService(NotificationManager.class), "channelId");

        long notificationIntervalMinutes = 1;

        // THIS IS WHERE WE GET DELAY FOR NOTIFICATIONS
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String notificationSettingsJson = sharedPreferences.getString("settings", "");
        SettingsModel notificationSettings = new Gson().fromJson(notificationSettingsJson, SettingsModel.class);

        if (notificationSettings == null) {
            Log.e("DEBUG", "Cannot find settings - leaving default notification interval of 1 minute");
        } else {
            notificationIntervalMinutes = notificationSettings.getNotificationIntervalInMinutes();
            Log.e("DEBUG", "Setting notification interval for " + notificationIntervalMinutes + " minutes");
        }

        Intent notificationIntent = new Intent(this, PriceUpdateReceiver.class);

        // THIS IS RESPONSIBLE FOR BROADCASTING AND SENDING NOTIFICATIONS AN SPECIFIED TIME. RIGHT NOW IT'S EVERY MINUTE
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60 * notificationIntervalMinutes, pendingIntent);
    }

    private int getSelectedItemPosition(int itemId) {
        if (itemId == ACTION_LIST) {
            return 0;
        } else if (itemId == ACTION_FOLLOWED) {
            return 1;
        } else if (itemId == ACTION_SETTINGS) {
            return 2;
        }
        return 0;
    }

    private int getSelectedItemId(int position) {
        if (position == 0) {
            return ACTION_LIST;
        } else if (position == 1) {
            return ACTION_FOLLOWED;
        } else if (position == 2) {
            return ACTION_SETTINGS;
        }
        return 0;
    }

    private void handleLogout() {
        Intent intent = new Intent(HomepageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getAvailableItems() {
        Call<List<ItemResponse>> itemsCall = ItemServiceProvider.getInstance().getItems();
        itemsCall.enqueue(new Callback<List<ItemResponse>>() {
            @Override
            public void onResponse(Call<List<ItemResponse>> call, Response<List<ItemResponse>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for getItems");
                    return;
                }
                List<ItemResponse> allItems = response.body();
                itemViewModel.setAllItems(allItems);
                getFollowedItems();
                Log.d("DEBUG", "All items: " + allItems.toString());
            }

            @Override
            public void onFailure(Call<List<ItemResponse>> call, Throwable t) {
                Log.e("ERROR", "Couldn't get available items", t);
            }
        });
    }

    private void getFollowedItems() {
        Call<List<ItemResponse>> followedCall = ItemServiceProvider.getInstance().getFollowedItems();
        followedCall.enqueue(new Callback<List<ItemResponse>>() {
            @Override
            public void onResponse(Call<List<ItemResponse>> call, Response<List<ItemResponse>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for getFollowedItems");
                    return;
                }
                List<ItemResponse> followedItems = response.body();
                itemViewModel.setFollowedItems(followedItems);
                getNotFollowedItems();
                Log.d("DEBUG", "Followed: " + followedItems.toString());
            }

            @Override
            public void onFailure(Call<List<ItemResponse>> call, Throwable t) {
                Log.e("ERROR", "Couldn't get followed items", t);
            }
        });
    }

    private void getNotFollowedItems() {
        List<ItemResponse> allItems = itemViewModel.getAllItemsLiveData().getValue();
        List<ItemResponse> followedItems = itemViewModel.getFollowedItemsLiveData().getValue();

        if (allItems != null && followedItems != null) {
            List<ItemResponse> notFollowedItems = new ArrayList<>(allItems);
            notFollowedItems.removeAll(followedItems);

            // Zapisz dane do ViewModel
            itemViewModel.setNotFollowedItems(notFollowedItems);
            Log.d("DEBUG", "Added not followed: " + notFollowedItems.toString());
        }
    }

    public static void updateItemModel(List<ItemResponse> items) {
        itemViewModel.update(items);
    }
}
