<<<<<<< HEAD
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
import com.example.pricetracker.components.CustomToast;
import com.example.pricetracker.dto.request.RegistrationRequest;
import com.example.pricetracker.dto.response.AuthTokenResponse;
import com.example.pricetracker.dto.response.SignupResponse;

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
        Call<SignupResponse> call = AuthorizationServiceProvider
                .getInstance()
                .registerUser(new RegistrationRequest(username, email, password));
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    CustomToast.showToastShort(SignupActivity.this, "Email already used");
                    Log.e("ERROR", "Bad request for signup");
                    return;
                }
                final SignupResponse signupResponse = response.body();
                try {
                    AuthorizationUtils.setAuthorizationData(signupResponse.getToken());
                    Log.i("SIGNUP SUCCESS", "User successfully signed up");
                    startActivity(new Intent(SignupActivity.this, HomepageActivity.class));
                } catch (AuthTokenException e) {
                    Log.e("AUTHORIZATION FAILED", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                CustomToast.showToastShort(SignupActivity.this, "Failed to sign up");
                Log.e("ERROR", "Couldn't  sign up", t);
            }
        });
    }
=======
package com.example.pricetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pricetracker.api.headers.AuthTokenException;
import com.example.pricetracker.api.headers.AuthorizationProvider;
import com.example.pricetracker.api.provider.AuthorizationServiceProvider;
import com.example.pricetracker.components.CustomToast;
import com.example.pricetracker.dto.request.RegistrationRequest;
import com.example.pricetracker.dto.response.SignupResponse;

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
        Call<SignupResponse> call = AuthorizationServiceProvider
                .getInstance()
                .registerUser(new RegistrationRequest(username, email, password));
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    CustomToast.showToastShort(SignupActivity.this, "Email already used");
                    Log.e("ERROR", "Bad request for signup");
                    return;
                }
                final SignupResponse signupResponse = response.body();
                try {
                    AuthorizationProvider.getInstance().setAuthorizationData(signupResponse.getToken());
                    Log.i("SIGNUP SUCCESS", "User successfully signed up");
                    startActivity(new Intent(SignupActivity.this, HomepageActivity.class));
                } catch (AuthTokenException e) {
                    Log.e("AUTHORIZATION FAILED", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                CustomToast.showToastShort(SignupActivity.this, "Failed to sign up");
                Log.e("ERROR", "Couldn't  sign up", t);
            }
        });
    }
>>>>>>> 65b55aacd8480322c23cf22321aea25884d4a02f
}