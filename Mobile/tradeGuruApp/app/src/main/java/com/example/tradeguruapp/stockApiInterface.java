package com.example.tradeguruapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface stockApiInterface {

    String BASE_URL = "https://www.alphavantage.co/";


    @GET("query?")
    Call<metaData> getMetaData (
            @Query("function") String timeSeries,
            @Query("symbol") String symbol,
            @Query("interval") String interval,
            @Query("apikey") String apikey
    );
}
