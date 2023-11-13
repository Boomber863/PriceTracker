package com.example.pricetracker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);

        // Retrieve item name from the intent
        String itemName = getIntent().getStringExtra("itemName");

        // Set the item name in the TextView
        if (itemName != null) {
            itemNameTextView.setText(itemName);
        }
    }
}
