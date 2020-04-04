package com.example.retrofit;

import com.google.gson.annotations.SerializedName;

public class Retrophoto {
    @SerializedName("key")
    private String key;
    @SerializedName("place_id")
    private String place_id;

    public void RetroPhoto(String key, String place_id) {
        this.key=key;
        this.place_id=place_id;
    }

    public String getKey() {
        return key;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}

