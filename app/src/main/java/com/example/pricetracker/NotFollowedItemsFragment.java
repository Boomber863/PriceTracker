package com.example.pricetracker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pricetracker.components.ItemViewModel;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

public class NotFollowedItemsFragment extends Fragment {

    private ItemViewModel itemViewModel;
    private ArrayAdapter<ItemResponse> notFollowedItemsAdapter;
    private ListView notFollowedItemsListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        notFollowedItemsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);

        // Obserwuj zmiany w danych niefollowowanych itemów
        itemViewModel.getNotFollowedItemsLiveData().observe(this, notFollowedItems -> {
            // Zaktualizuj adapter i wyświetl listę
            notFollowedItemsAdapter.clear();
            notFollowedItemsAdapter.addAll(notFollowedItems);
            notFollowedItemsAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_followed_items, container, false);

        notFollowedItemsListView = view.findViewById(R.id.notFollowedItemsListView);
        notFollowedItemsListView.setAdapter(notFollowedItemsAdapter);

        return view;
    }

    // ... reszta kodu
}