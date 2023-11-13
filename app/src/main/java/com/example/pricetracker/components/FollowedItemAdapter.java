package com.example.pricetracker.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pricetracker.R;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;
import java.util.Objects;

public class FollowedItemAdapter extends ArrayAdapter<ItemResponse> {

    private final Context context;
    private final int resource;
    private final FollowedItemsActionsListener followedItemsActionsListener;

    public FollowedItemAdapter(Context context,
                               int resource,
                               List<ItemResponse> objects,
                               FollowedItemsActionsListener followedItemsActionsListener) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.followedItemsActionsListener = followedItemsActionsListener; // Initialize the listener
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView itemNameTextView = convertView.findViewById(R.id.followedItemName);
        Button unfollowButton = convertView.findViewById(R.id.unfollowButton);

        ItemResponse item = getItem(position);

        if (item == null) {
            return convertView;
        }

        itemNameTextView.setText(item.getName());
        itemNameTextView.setOnClickListener(v ->
                followedItemsActionsListener.goToItemDetailsActivity(item));
        unfollowButton.setOnClickListener(v ->
                followedItemsActionsListener.onUnfollowItem(item));

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).getId();
    }
}

