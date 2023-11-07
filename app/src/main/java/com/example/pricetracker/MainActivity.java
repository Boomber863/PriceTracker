package com.example.pricetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SignupActivity.class)));
    }
}