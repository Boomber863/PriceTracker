package com.example.pricetracker;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/auth/signup/") // Endpoint do rejestracji
    Call<User> registerUser(@Body RegistrationRequest registrationRequest);

    @POST("/auth/signin/") // Endpoint do logowania
    Call<AuthToken> loginUser(@Body LoginRequest loginRequest);
}
