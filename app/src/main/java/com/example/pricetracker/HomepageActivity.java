package com.example.pricetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.components.FollowedItemAdapter;
import com.example.pricetracker.components.FollowedItemsActionsListener;
import com.example.pricetracker.components.ItemViewModel;
import com.example.pricetracker.dto.request.FollowItemRequest;
import com.example.pricetracker.dto.response.FollowedItemResponse;
import com.example.pricetracker.dto.response.ItemResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity{

    private static final int ACTION_LIST = R.id.action_list;
    private static final int ACTION_FOLLOWED = R.id.action_followed;
    private static final int ACTION_SETTINGS = R.id.action_settings;

    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        loadFragment(new NotFollowedItemsFragment());

        // Dodaj Bottom Navigation View Listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == ACTION_LIST) {
                    loadFragment(new NotFollowedItemsFragment());
                    return true;
                } else if (itemId == ACTION_FOLLOWED) {
                    loadFragment(new FollowedItemsFragment());
                    return true;
                } else if (itemId == ACTION_SETTINGS) {
                    loadFragment(new SettingsFragment());
                    return true;
                }
                return false;
            }
        });


        getAvailableItems();
/*
        // ALL ITEMS IN DATABASE
        items = new ArrayList<>();
        // ITEMS FOLLOWED BY USER
        followed = new ArrayList<>();
        // NOT FOLLOWED ITEMS
        notFollowed = new ArrayList<>();

        followedItemAdapter = new FollowedItemAdapter(this, R.layout.list_item_followed, followed, this);
        notFollowedSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, notFollowed);
        notFollowedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        followedListView = findViewById(R.id.followedList);
        followedListView.setAdapter(followedItemAdapter);

        notFollowedSpinner = findViewById(R.id.notFollowedSpinner);
        notFollowedSpinner.setAdapter(notFollowedSpinnerAdapter);

        notFollowedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ItemResponse selectedItem = (ItemResponse) parentView.getItemAtPosition(position);
                if (selectedItem != null) {
                    // Do something with the selected item if needed
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        followedListView.setOnItemClickListener((parent, view, position, id) -> {
            ItemResponse clickedItem = followedItemAdapter.getItem(position);
            if (clickedItem != null) {
                // Create an Intent to start ItemDetailsActivity
                Intent intent = new Intent(HomepageActivity.this, ItemDetailsActivity.class);

                // Pass the item name as an extra
                intent.putExtra("itemName", clickedItem.getName());
                intent.putExtra("itemId", clickedItem.getId());

                // Start the new activity
                startActivity(intent);
            }
        });

        Button followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(v -> toggleFollowState());

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> handleLogout());
*/
    }

    // Metoda do ładowania fragmentów
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

/*
    private void handleLogout() {

        Intent intent = new Intent(HomepageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/
/*
    private void toggleFollowState() {
        ItemResponse selectedItem = (ItemResponse) notFollowedSpinner.getSelectedItem();
        if (selectedItem != null) {
            followItem(selectedItem);
        }
    }*/

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
/*
    @Override
    public void onUnfollowItem(ItemResponse item) {
        unfollowItem(item);
    }

    @Override
    public void goToItemDetailsActivity(ItemResponse item) {
        // Create an Intent to start ItemDetailsActivity
        Intent intent = new Intent(HomepageActivity.this, ItemDetailsActivity.class);

        // Pass the item name as an extra
        intent.putExtra("itemName", item.getName());
        intent.putExtra("itemId", item.getId());

        // Start the new activity
        startActivity(intent);
    }

    private void refreshAdapters() {
        followedItemAdapter.notifyDataSetChanged();
        notFollowedSpinnerAdapter.notifyDataSetChanged();
    }

    private void followItem(ItemResponse selectedItem) {

        FollowItemRequest followItemRequest = new FollowItemRequest(selectedItem.getId());
        Call<FollowedItemResponse> followCall = ItemServiceProvider.getInstance().followItem(followItemRequest);
        followCall.enqueue(new Callback<FollowedItemResponse>() {
            @Override
            public void onResponse(Call<FollowedItemResponse> call, Response<FollowedItemResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for followItem");
                    return;
                }
                followed.add(selectedItem);
                notFollowed.remove(selectedItem);

                followedItemAdapter.notifyDataSetChanged();
                notFollowedSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FollowedItemResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't follow item", t);
            }
        });
    }

    private void unfollowItem(ItemResponse selectedItem) {

        FollowItemRequest unfollowItemRequest = new FollowItemRequest(selectedItem.getId());
        Call<FollowedItemResponse> unfollowCall = ItemServiceProvider.getInstance().unfollowItem(unfollowItemRequest);
        unfollowCall.enqueue(new Callback<FollowedItemResponse>() {
            @Override
            public void onResponse(Call<FollowedItemResponse> call, Response<FollowedItemResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for unfollowItem");
                    return;
                }
                followed.remove(selectedItem);
                notFollowed.add(selectedItem);

                followedItemAdapter.notifyDataSetChanged();
                notFollowedSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FollowedItemResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't unfollow item", t);
            }
        });
    }
*/
}