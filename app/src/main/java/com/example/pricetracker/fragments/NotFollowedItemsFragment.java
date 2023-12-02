package com.example.pricetracker.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pricetracker.HomepageActivity;
import com.example.pricetracker.ItemDetailsActivity;
import com.example.pricetracker.R;
import com.example.pricetracker.components.ItemViewModel;

import java.util.ArrayList;

public class NotFollowedItemsFragment extends Fragment {

    private ItemViewModel itemViewModel;
    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Observe changes in the list of not followed items
        itemViewModel.getNotFollowedItemsLiveData().observe(this, notFollowedItems -> {
            // Update the adapter with the new list of items
            itemAdapter.updateItemList(notFollowedItems);
        });

        // Observe changes in the list of not followed items
        itemViewModel.getFollowedItemsLiveData().observe(this, followedItems -> {
            // Update the adapter with the new list of items
            itemAdapter.updateFollowedItemList(followedItems);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_followed_items, container, false);

        // Initialize RecyclerView and set its adapter
        itemsRecyclerView = view.findViewById(R.id.recyclerViewList);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        itemAdapter = new ItemAdapter(new ArrayList<>());
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
}