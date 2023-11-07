package com.example.pricetracker.dto.request;

public class RegistrationRequest {

    private final String username;
    private final String email;
    private final String password;

    public RegistrationRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
