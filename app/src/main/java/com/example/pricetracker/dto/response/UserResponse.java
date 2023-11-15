package com.example.pricetracker.dto.response;

public class UserResponse {

    private String email;
    private String username;
    private int id;
    private boolean is_active;
    private String role;
    private boolean is_subscribed;

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return is_active;
    }

    public boolean isSubscribed() {
        return is_subscribed;
    }
}
