package com.example.pricetracker.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pricetracker.R;
import com.example.pricetracker.api.ServerUrls;
import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.dto.request.FollowItemRequest;
import com.example.pricetracker.dto.response.FollowedItemResponse;
import com.example.pricetracker.dto.response.ItemPriceResponse;
import com.example.pricetracker.dto.response.ItemResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemResponse> itemList;
    private List<ItemResponse> followedItemList;
    private OnItemClickListener onItemClickListener;

    public ItemAdapter(List<ItemResponse> itemList) {
        this.itemList = itemList;
        this.followedItemList = itemList;
    }

    public void updateItemList(List<ItemResponse> newList) {
        itemList = newList;
        notifyDataSetChanged();
    }

    public void updateFollowedItemList(List<ItemResponse> newList) {
        followedItemList = newList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(ItemResponse item);
    }

    // Setter method for click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemResponse item = itemList.get(position);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });

        holder.productNameTextView.setText(item.getName());

        ItemPriceResponse newestPrice = item.getNewestPrice();
        String priceText = "Price: " + (newestPrice == null ? 0.00 :
                Math.round(newestPrice.getPrice() * 100.0) / 100.0) + " zl";
        holder.productPriceTextView.setText(priceText);

        String url = item.getImageUrl();
        if (url == null) {
            url = "http://10.0.2.2:8000/static/product_img/example.jpg";
        }
        // THIS ENSURES THAT URL IS ACCESSIBLE FROM ANDROID EMULATOR
        else if (url.startsWith("http://127.0.0.1")) {
            url = url.replace("http://127.0.0.1:8000", ServerUrls.SERVER_URL);
        }

        Picasso.get()
                .load(url)
                .resize(300, 300)
                .placeholder(R.drawable.baseline_downloading_24)
                .error(R.drawable.baseline_error_24)
                .into(holder.productImageView);

        //check if item is followed
        if (followedItemList.contains(item)) {
            holder.imageButton.setImageResource(R.drawable.baseline_star_24);
        } else {
            holder.imageButton.setImageResource(R.drawable.baseline_star_border_24);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followedItemList.contains(item)) {
                    unfollowItem(item);
                    holder.imageButton.setImageResource(R.drawable.baseline_star_border_24);
                } else {
                    followItem(item);
                    holder.imageButton.setImageResource(R.drawable.baseline_star_24);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //zdjecie sie ogarnie
            productImageView = itemView.findViewById(R.id.productImage);
            productNameTextView = itemView.findViewById(R.id.productName);
            productPriceTextView = itemView.findViewById(R.id.productPrice);
            imageButton = itemView.findViewById(R.id.imageButton);
        }
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
                notifyDataSetChanged();
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
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FollowedItemResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't unfollow item", t);
            }
        });
    }
}
