package com.example.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {
//ChIJS29cpv0FGTkRU2RDEsHu2CI
        @GET("/restaurantProvider")
        Call<List<Retrophoto>> getpersonalized(@Query("query") String ID);

    @GET("/furnitureProvider")
    Call<List<Retrophoto>> getpersonalizedfurn(@Query("query") String ID);
//
//        @GET("/personalisedProvider")
//        Call<List<Retrophoto>> getpersonalized(@Query("ID") String ID);
    }

