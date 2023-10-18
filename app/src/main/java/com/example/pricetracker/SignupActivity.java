package com.example.pricetracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pricetracker.api.provider.AuthenticationServiceProvider;
import com.example.pricetracker.dto.request.RegistrationRequest;
import com.example.pricetracker.dto.response.UserResponse;

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
        Call<UserResponse> call = AuthenticationServiceProvider
                .getInstance()
                .getAuthenticationService()
                .registerUser(new RegistrationRequest(username,email, password));
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse user = response.body();
                    Log.i("SUCCESS", user.getUsername() + " signed up!");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("ERROR", "Couldn't  sign up", t);
            }
        });
    }
}