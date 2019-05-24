package com.teocfish.teoc;

import retrofit.RestAdapter;

public class Api {

    public static ApiInterface getClient() {
        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://chilikaxi.com/admin") //Set the Root URL
                .build(); //Finally building the adapter
        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
