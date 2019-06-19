package com.teocfish.teoc;

import com.teocfish.teoc.ApiInterface;

import retrofit.RestAdapter;

public class Api {

    public static ApiInterface getClient() {
        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://teocfish.com/adminpanel") //Set the Root URL
                .build(); //Finally building the adapter
        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
