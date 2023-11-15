package com.example.pricetracker.dto.response;

public class AuthTokenResponse {

    private String access_token;
    private String token_type;

    public AuthTokenResponse() {

    }

    public AuthTokenResponse(String token) {
        this.access_token = token;
        this.token_type = "bearer";
    }

    public String getAccessToken() {
        return access_token;
    }
}
