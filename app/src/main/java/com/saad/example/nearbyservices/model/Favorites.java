package com.saad.example.nearbyservices.model;

/**
 * Created by Team Mashwara on 3/3/2020.
 */
public class Favorites {
    private String  Place_name, placeID;
    public Favorites(String Place_name,String placeID)
    {

        this.Place_name=Place_name;
        this.placeID=placeID;
    }
    public String getPlace_name() {
        return Place_name;
    }

    public String getPlaceID() {
        return placeID;
    }


}
