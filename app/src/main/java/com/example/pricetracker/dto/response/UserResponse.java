package com.example.pricetracker.dto.response;

public class UserResponse {

    private String username;
    private String email;
    private int id;
    private boolean is_active;
    private String role;
    private boolean is_subscribed;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return is_active;
    }

    public String getRole() {
        return role;
    }

    public boolean isSubscribed() {
        return is_subscribed;
    }
}
