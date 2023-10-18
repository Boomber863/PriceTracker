package com.example.pricetracker.api;

import com.example.pricetracker.dto.response.AuthTokenResponse;
import com.example.pricetracker.dto.request.LoginRequest;
import com.example.pricetracker.dto.request.RegistrationRequest;
import com.example.pricetracker.dto.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("/auth/signup")
    Call<UserResponse> registerUser(@Body RegistrationRequest registrationRequest);

    @POST("/auth/signin")
    Call<AuthTokenResponse> loginUser(@Body LoginRequest loginRequest);
}
