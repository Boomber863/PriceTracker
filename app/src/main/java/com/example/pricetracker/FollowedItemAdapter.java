package com.example.pricetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;
import java.util.Objects;

public class FollowedItemAdapter extends ArrayAdapter<ItemResponse> {

    private Context context;
    private int resource;
    private UnfollowItemListener unfollowItemListener;

    public FollowedItemAdapter(@NonNull Context context, int resource, @NonNull List<ItemResponse> objects, UnfollowItemListener unfollowItemListener) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.unfollowItemListener = unfollowItemListener; // Initialize the listener
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView itemNameTextView = convertView.findViewById(R.id.followedItemName);
        Button unfollowButton = convertView.findViewById(R.id.unfollowButton);

        ItemResponse item = getItem(position);

        if (item != null) {
            itemNameTextView.setText(item.getName());

            // Set onClickListener for the unfollow button
            unfollowButton.setOnClickListener(v -> {
                // Notify the listener when the Unfollow button is clicked
                if (unfollowItemListener != null) {
                    unfollowItemListener.onUnfollowItem(item);
                }
            });
        }

        return convertView;
    }

    public interface UnfollowItemListener {
        void onUnfollowItem(ItemResponse item);
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

