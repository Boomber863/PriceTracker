package com.example.pricetracker.fragments;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pricetracker.ItemDetailsActivity;
import com.example.pricetracker.R;
import com.example.pricetracker.components.ItemViewModel;
import com.example.pricetracker.dto.response.ItemResponse;
import com.example.pricetracker.notifications.NotificationSender;
import com.example.pricetracker.notifications.PriceUpdateReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FollowedItemsFragment extends Fragment {

    private ItemViewModel itemViewModel;
    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private EditText editTextSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Observe changes in the list of not followed items
        itemViewModel.getFollowedItemsLiveData().observe(this, followedItems -> {
            itemAdapter.updateItemList(followedItems);
            itemAdapter.updateFollowedItemList(followedItems);
        });
        prepareNotificationSystem();
    }

    private void prepareNotificationSystem() {
        // THIS CREATES NOTIFICATION CHANNEL
        NotificationSender.getInstance()
                .createNotificationChannel(requireContext().getSystemService(NotificationManager.class), "channelId");

        // TODO: FIGURE OUT HOW TO UPDATE DATA IN INTENT
        Intent notificationIntent = new Intent(requireContext(), PriceUpdateReceiver.class);
        notificationIntent.putExtra("items", new ArrayList<>(itemViewModel.getFollowedItemsLiveData().getValue()));

        // THIS IS RESPONSIBLE FOR BROADCASTING AND SENDING NOTIFICATIONS AN SPECIFIED TIME. RIGHT NOW IT'S EVERY MINUTE
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (requireContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                1000 * 60, pendingIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followed_items, container, false);

        // Initialize RecyclerView and set its adapter
        itemsRecyclerView = view.findViewById(R.id.recyclerViewList);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        editTextSearch = view.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterItems(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        itemAdapter = new ItemAdapter(new ArrayList<>(), new ArrayList<>(), itemViewModel);
        // Set click listener for the adapter
        itemAdapter.setOnItemClickListener(item -> {
            // Create an Intent to start ItemDetailsActivity
            Intent intent = new Intent(requireContext(), ItemDetailsActivity.class);

            // Pass the item name as an extra
            intent.putExtra("itemName", item.getName());
            intent.putExtra("itemId", item.getId());

            // Start the new activity
            startActivity(intent);
        });
        itemsRecyclerView.setAdapter(itemAdapter);

        return view;
    }

    private void filterItems(String searchText) {
        List<ItemResponse> filteredList = new ArrayList<>();
        for (ItemResponse item : Objects.requireNonNull(itemViewModel.getFollowedItemsLiveData().getValue())) {
            if (item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        itemAdapter.updateItemList(filteredList);
    }
}