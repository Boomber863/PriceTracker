package com.example.pricetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<ItemResponse> itemsAdapter;
    private List<ItemResponse> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, R.layout.list_item, items);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(itemsAdapter);

        getAvailableItems();
    }

    private void getAvailableItems() {
        Call<List<ItemResponse>> itemsCall = ItemServiceProvider.getInstance().getItems();
        itemsCall.enqueue(new Callback<List<ItemResponse>>() {
            @Override
            public void onResponse(Call<List<ItemResponse>> call, Response<List<ItemResponse>> response) {
                if(!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for getItems");
                    return;
                }
                items.addAll(response.body());
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ItemResponse>> call, Throwable t) {
                Log.e("ERROR", "Couldn't get available items", t);
            }
        });
    }
}