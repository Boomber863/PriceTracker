package com.example.pricetracker.dto.response;

public class SignupResponse {

    private UserResponse user;
    private String token;

    public UserResponse getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
