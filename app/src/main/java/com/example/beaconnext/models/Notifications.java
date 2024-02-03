package com.example.beaconnext.models;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.QueryMap;

public class Notifications {
    public Notifications(int id, String notifications) {
        this.id = id;
        this.notifications = notifications;
    }

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @SerializedName("title")
    String notifications;

    public String getnotifications() {
        return notifications;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "id=" + id +
                ", notifications='" + notifications + '\'' +
                '}';
    }

    public void setnotifications(String notifications) {
        this.notifications = notifications;
    }

    public Notifications() {
    }
}
