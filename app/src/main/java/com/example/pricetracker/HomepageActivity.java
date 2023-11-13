package com.example.pricetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.dto.request.FollowItemRequest;
import com.example.pricetracker.dto.response.FollowedItemResponse;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity implements FollowedItemAdapter.UnfollowItemListener {

    private ArrayAdapter<ItemResponse> itemsAdapter;
    private List<ItemResponse> items;
    private ArrayAdapter<ItemResponse> followedAdapter;
    private List<ItemResponse> followed;

    private ArrayAdapter<ItemResponse> notFollowedSpinnerAdapter;
    private List<ItemResponse> notFollowed;
    private Spinner notFollowedSpinner;
    private ListView followedListView;
    private FollowedItemAdapter followedItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);



        items = new ArrayList<>();
        followed = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, R.layout.list_item_followed, items);
        followedAdapter = new ArrayAdapter<>(this, R.layout.list_item_followed, followed);

        notFollowed = new ArrayList<>();
        notFollowedSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, notFollowed);
        notFollowedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notFollowedSpinner = findViewById(R.id.notFollowedSpinner);
        followedListView = findViewById(R.id.followedList);
        notFollowedSpinner.setAdapter(notFollowedSpinnerAdapter);
        followedListView.setAdapter(followedAdapter);


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

        followedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Retrieve the clicked item
                ItemResponse clickedItem = followedAdapter.getItem(position);

                if (clickedItem != null) {
                    // Create an Intent to start ItemDetailsActivity
                    Intent intent = new Intent(HomepageActivity.this, ItemDetailsActivity.class);

                    // Pass the item name as an extra
                    intent.putExtra("itemName", clickedItem.getName());
                    intent.putExtra("itemId", clickedItem.getId());

                    // Start the new activity
                    startActivity(intent);
                }
            }
        });


        Button followButton = findViewById(R.id.followButton);
        followButton.setOnClickListener(v -> toggleFollowState());

        getAvailableItems();
        getFollowedItems();
    }

    private void toggleFollowState() {
        ItemResponse selectedItem = (ItemResponse) notFollowedSpinner.getSelectedItem();
        if (selectedItem != null) {
            followItem(selectedItem);
        }
    }

    private void getAvailableItems() {
        Call<List<ItemResponse>> itemsCall = ItemServiceProvider.getInstance().getItems();
        itemsCall.enqueue(new Callback<List<ItemResponse>>() {
            @Override
            public void onResponse(Call<List<ItemResponse>> call, Response<List<ItemResponse>> response) {
                if(!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for getItems");
                    return;
                }
                items.clear();
                items.addAll(response.body());
                itemsAdapter.notifyDataSetChanged();
                getNotFollowedItems();

                Log.d("DEBUG", "All items: " + items.toString());
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
                if(!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for getFollowedItems");
                    return;
                }
                followed.clear();
                followed.addAll(response.body());
                followedAdapter.notifyDataSetChanged();
                getNotFollowedItems();
                updateFollowedListView();

                Log.d("DEBUG", "Followed: " + followed.toString());
            }

            @Override
            public void onFailure(Call<List<ItemResponse>> call, Throwable t) {
                Log.e("ERROR", "Couldn't get followed items", t);
            }
        });
    }

    private void updateFollowedListView() {
        if (followedItemAdapter == null) {
            followedItemAdapter = new FollowedItemAdapter(this, R.layout.list_item_followed, followed, this);
            followedListView.setAdapter(followedItemAdapter);
        } else {
            followedItemAdapter.clear();
            followedItemAdapter.addAll(followed);
            followedItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUnfollowItem(ItemResponse item) {
        unfollowItem(item);
    }

    private void getNotFollowedItems() {
        notFollowed.clear();

        for (ItemResponse item : items) {
            if (!followed.contains(item)) {
                notFollowed.add(item);
            }
        }

        notFollowedSpinnerAdapter.notifyDataSetChanged();

        Log.d("DEBUG", "Added not followed: " + notFollowed.toString());
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

                followedAdapter.notifyDataSetChanged();
                notFollowedSpinnerAdapter.notifyDataSetChanged();

                if (followedItemAdapter == null) {
                    followedItemAdapter = new FollowedItemAdapter(HomepageActivity.this, R.layout.list_item_followed, followed, HomepageActivity.this);
                    followedListView.setAdapter(followedItemAdapter);
                } else {
                    followedItemAdapter.notifyDataSetChanged();
                }
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

                followedAdapter.notifyDataSetChanged();
                notFollowedSpinnerAdapter.notifyDataSetChanged();

                if (followedItemAdapter == null) {
                    followedItemAdapter = new FollowedItemAdapter(HomepageActivity.this, R.layout.list_item_followed, followed, HomepageActivity.this);
                    followedListView.setAdapter(followedItemAdapter);
                } else {
                    followedItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FollowedItemResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't unfollow item", t);
            }
        });
    }

}