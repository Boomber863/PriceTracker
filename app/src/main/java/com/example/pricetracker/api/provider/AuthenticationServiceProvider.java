package com.example.pricetracker.api.provider;

import com.example.pricetracker.api.AuthenticationService;
import com.example.pricetracker.api.ServerUrls;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationServiceProvider {

    private static AuthenticationServiceProvider instance = null;
    private final AuthenticationService authenticationService;

    private AuthenticationServiceProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUrls.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.authenticationService = retrofit.create(AuthenticationService.class);
    }

    public static AuthenticationServiceProvider getInstance() {
        if(instance == null) {
            instance = new AuthenticationServiceProvider();
        }
        return instance;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}
