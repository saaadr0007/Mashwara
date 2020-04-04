package com.example.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Team Mashwara on 1/2/2020.
 */
public class Pojo {
    @SerializedName("key")
    private String key;
    @SerializedName("description")
    private String descripion;

    public Pojo(String key, String place_id) {
        this.key = key;
        this.descripion = place_id;
    }

    public String getKey() {
        return key;
    }

    public String getDescripion() {
        return descripion;
    }

}