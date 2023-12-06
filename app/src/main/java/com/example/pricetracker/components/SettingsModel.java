package com.example.pricetracker.components;

import java.util.Arrays;

public class SettingsModel {
    boolean enableNotifications;
    int notificationIntervalInMinutes;

    public SettingsModel(boolean enableNotifications, int notificationIntervalInMinutes) {
        this.enableNotifications = enableNotifications;
        this.notificationIntervalInMinutes = notificationIntervalInMinutes;
    }

    public void setEnableNotifications(boolean enabled) {
        this.enableNotifications = enabled;
    }

    public boolean areNotificationsEnabled() {
        return this.enableNotifications;
    }

    public int getNotificationIntervalInMinutes() {
        return this.notificationIntervalInMinutes;
    }

    public void setNotificationInterval(int minutes) {
        this.notificationIntervalInMinutes = minutes;
    }

    public String[] getOptions() {
        String[] values = {
                "10 minutes",
                "30 minutes",
                "1 hour",
                "6 hours",
                "1 day",
        };
        return values;
    }

    public String getOptionForMinutes(int minutes) {
        switch(minutes) {
            case 10: return "10 minutes";
            case 30: return "30 minutes";
            case 60: return "1 hour";
            case 360: return "6 hours";
            case 1440: return "1 day";
        }
        return "10 minutes";
    }

    public int getMinutesForOption(String option) {
        switch(option) {
            case "10 minutes": return 10;
            case "30 minutes": return 30;
            case "1 hour": return 60;
            case "6 hours": return 360;
            case "1 day": return 1440;
        }
        return 10;
    }

    public int getCurrentOptionIndex() {
        String currentOption = this.getOptionForMinutes(this.notificationIntervalInMinutes);
        return Arrays.asList(this.getOptions()).indexOf(currentOption);
    }
}
