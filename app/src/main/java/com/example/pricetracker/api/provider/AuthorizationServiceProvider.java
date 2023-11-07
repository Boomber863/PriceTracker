package com.example.pricetracker.api.provider;

import com.example.pricetracker.api.AuthorizationService;
import com.example.pricetracker.api.ServerUrls;
import com.example.pricetracker.dto.request.LoginRequest;
import com.example.pricetracker.dto.request.RegistrationRequest;
import com.example.pricetracker.dto.response.AuthTokenResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthorizationServiceProvider {

    private static AuthorizationServiceProvider instance = null;
    private final AuthorizationService authorizationService;

    private AuthorizationServiceProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUrls.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.authorizationService = retrofit.create(AuthorizationService.class);
    }

    public static AuthorizationServiceProvider getInstance() {
        if(instance == null) {
            instance = new AuthorizationServiceProvider();
        }
        return instance;
    }

    public Call<AuthTokenResponse> loginUser(LoginRequest loginRequest) {
        return authorizationService.loginUser(loginRequest);
    }

    public Call<AuthTokenResponse> registerUser(RegistrationRequest registrationRequest) {
        return authorizationService.registerUser(registrationRequest);
    }
}
