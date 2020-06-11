package com.saad.example.nearbyservices.model;

/**
 * Created by Team Mashwara on 6/10/2020.
 */
public class Filter {
    String item;
    Filter()
    {

    }

    public Filter(String item)
    {
        this.item=item;
    }
    public String getFilter() {
        return item;
    }

    public void setFilter(String filter) {
        this.item = item;
    }
}
