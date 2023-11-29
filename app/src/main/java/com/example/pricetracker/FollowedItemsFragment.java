package com.example.pricetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pricetracker.components.ItemViewModel;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

public class FollowedItemsFragment extends Fragment {

    private ItemViewModel itemViewModel;
    private ArrayAdapter<ItemResponse> followedItemsAdapter;
    private ListView followedItemsListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        followedItemsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);

        // Obserwuj zmiany w danych niefollowowanych itemów
        itemViewModel.getFollowedItemsLiveData().observe(this, followedItems -> {
            // Zaktualizuj adapter i wyświetl listę
            followedItemsAdapter.clear();
            followedItemsAdapter.addAll(followedItems);
            followedItemsAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followed_items, container, false);

        followedItemsListView = view.findViewById(R.id.followedItemsListView);
        followedItemsListView.setAdapter(followedItemsAdapter);

        return view;
    }

    // ... reszta kodu
}