package com.example.pricetracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pricetracker.api.provider.AuthenticationServiceProvider;
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
        Call<AuthTokenResponse> call = AuthenticationServiceProvider
                .getInstance()
                .getAuthenticationService()
                .loginUser(new LoginRequest(username, password));
        call.enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (response.isSuccessful()) {
                    AuthTokenResponse authTokenResponse = response.body();
                    Log.i("SUCCESS", "");
                }
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't log in", t);
            }
        });
    }
}