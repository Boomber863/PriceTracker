package com.example.pricetracker.api;

public class ServerUrls {

    // SERVER URL - 10.0.2.2 ALLOWS TO CONNECT TO LOCALHOST FROM PHONE EMULATOR
    public static String SERVER_URL = "http://10.0.2.2:8000";

    // ENDPOINTS

    public static String SIGN_UP = "/auth/signup/";
    public static String LOG_IN = "/auth/signin/";
    public static String FOLLOW_ITEM = "/follow";
    public static String UNFOLLOW_ITEM = "/unfollow";
    public static String GET_ITEMS = "/items";
    public static String GET_ITEM_PRICES = "/item_prices";
    public static String GET_FOLLOWED_ITEMS = "/followed_items";
}
