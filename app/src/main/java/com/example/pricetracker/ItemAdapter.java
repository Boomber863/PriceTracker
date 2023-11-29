package com.example.pricetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pricetracker.dto.response.ItemPriceResponse;
import com.example.pricetracker.dto.response.ItemResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemResponse> itemList;

    public ItemAdapter(List<ItemResponse> itemList) {
        this.itemList = itemList;
    }

    public void updateItemList(List<ItemResponse> newList) {
        itemList = newList;
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

        // Bind data to views in the item layout
        holder.productNameTextView.setText(item.getName());
        // Set other data as needed

        //zdjecie
        //nie dziala nw czemu
        Picasso.get()
                .load(item.getImageUrl())
                .resize(50, 50)
                .placeholder(R.drawable.baseline_sports_basketball_24)
                .error(R.drawable.baseline_star_24)
                .into(holder.productImageView);
        //Picasso.get().load(item.getImageUrl()).into(holder.productImageView);
        // Add click listeners or any other interactions if necessary
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
