package com.example.pricetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pricetracker.api.headers.AuthTokenException;
import com.example.pricetracker.api.headers.AuthorizationUtils;
import com.example.pricetracker.api.provider.AuthorizationServiceProvider;
import com.example.pricetracker.dto.request.LoginRequest;
import com.example.pricetracker.dto.response.AuthTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.loginUsername);
        passwordEditText = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(e -> onLoginClicked());
    }

    private void onLoginClicked() {

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Call<AuthTokenResponse> call = AuthorizationServiceProvider
                .getInstance()
                .loginUser(new LoginRequest(username, password));
        call.enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for login");
                    return;
                }
                final AuthTokenResponse authTokenResponse = response.body();
                try {
                    AuthorizationUtils.setAuthorizationData(authTokenResponse);
                    Log.i("LOGIN SUCCESS", "User successfully logged in");
                    startActivity(new Intent(LoginActivity.this, HomepageActivity.class));
                } catch (AuthTokenException e) {
                    Log.e("AUTHORIZATION FAILED", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't log in", t);
            }
        });
    }
}