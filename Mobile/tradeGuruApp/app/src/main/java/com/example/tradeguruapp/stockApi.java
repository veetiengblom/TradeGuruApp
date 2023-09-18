package com.example.tradeguruapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class stockApi {

    private static Retrofit retrofit = null;

    public static stockApiInterface getApiInterface() {

        if(retrofit==null) {
            retrofit = new Retrofit.Builder().baseUrl(stockApiInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(stockApiInterface.class);
    }
}