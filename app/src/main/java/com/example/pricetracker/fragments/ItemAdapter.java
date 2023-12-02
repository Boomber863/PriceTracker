package com.example.pricetracker.fragments;

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
import com.example.pricetracker.dto.response.ItemPriceResponse;
import com.example.pricetracker.dto.response.ItemResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemResponse> itemList;
    private List<ItemResponse> followedItemList;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemResponse item = itemList.get(position);

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
}
