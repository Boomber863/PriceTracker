package com.example.pricetracker.components;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pricetracker.R;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;
import java.util.Objects;

/**
 * ## NOT USED ##
 */
public class FollowedItemAdapter extends ArrayAdapter<ItemResponse> {

    private final Activity activity;
    private final int resource;
    private final FollowedItemsActionsListener followedItemsActionsListener;

    public FollowedItemAdapter(Activity activity,
                               int resource,
                               List<ItemResponse> objects,
                               FollowedItemsActionsListener followedItemsActionsListener) {
        super(activity, resource, objects);
        this.activity = activity;
        this.resource = resource;
        this.followedItemsActionsListener = followedItemsActionsListener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(resource, parent, false);
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

