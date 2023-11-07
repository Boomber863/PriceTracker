package com.example.pricetracker.dto.response;

public class AuthTokenResponse {

    private String access_token;
    private String token_type;
    private boolean is_subscribed;
    private String end_date;

    public String getAccessToken() {
        return access_token;
    }
}
