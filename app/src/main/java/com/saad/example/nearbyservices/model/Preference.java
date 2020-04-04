package com.saad.example.nearbyservices.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Team Mashwara on 3/20/2020.
 */
public class Preference {

    private String pref_name;

    public Preference(){

    }
    public String getPref_name() {
        return pref_name;
    }
    public Preference(String pref_name)
    {
        this.pref_name=pref_name;
    }
}
