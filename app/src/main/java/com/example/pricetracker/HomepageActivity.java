package com.example.pricetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.pricetracker.api.headers.AuthTokenException;
import com.example.pricetracker.api.headers.AuthorizationUtils;
import com.example.pricetracker.api.provider.ItemServiceProvider;
import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAvailableItems();
    }

    private void getAvailableItems() {
        Call<List<ItemResponse>> items = ItemServiceProvider.getInstance().getItems();
        items.enqueue(new Callback<List<ItemResponse>>() {
            @Override
            public void onResponse(Call<List<ItemResponse>> call, Response<List<ItemResponse>> response) {
                if(!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for getItems");
                    return;
                }
                String test = response.body().toString();
                Log.i("GOT ITEMS", test);
            }

            @Override
            public void onFailure(Call<List<ItemResponse>> call, Throwable t) {
                Log.e("ERROR", "Couldn't get available items", t);
            }
        });
    }
}