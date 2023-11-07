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
import com.example.pricetracker.dto.request.RegistrationRequest;
import com.example.pricetracker.dto.response.AuthTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.signupUsername);
        emailEditText = findViewById(R.id.signupEmail);
        passwordEditText = findViewById(R.id.signupPassword);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(e -> onSignUpClicked());
    }

    private void onSignUpClicked() {

        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Call<AuthTokenResponse> call = AuthorizationServiceProvider
                .getInstance()
                .registerUser(new RegistrationRequest(username, email, password));
        call.enqueue(new Callback<AuthTokenResponse>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("ERROR", "Bad response for signup");
                    return;
                }
                final AuthTokenResponse authTokenResponse = response.body();
                try {
                    AuthorizationUtils.setAuthorizationData(authTokenResponse);
                    Log.i("SIGNUP SUCCESS", "User successfully signed up");
                    startActivity(new Intent(SignupActivity.this, HomepageActivity.class));
                } catch (AuthTokenException e) {
                    Log.e("AUTHORIZATION FAILED", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't  sign up", t);
            }
        });
    }
}