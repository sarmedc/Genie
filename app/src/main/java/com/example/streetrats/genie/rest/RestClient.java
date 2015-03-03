package com.example.streetrats.genie.rest;

import com.example.streetrats.genie.rest.GenieService;

import retrofit.RestAdapter;

public class RestClient
{
    private static final String BASE_URL = "http://genie-server.herokuapp.com";
    private GenieService genieService;

    public RestClient()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        genieService = restAdapter.create(GenieService.class);
    }

    public GenieService getGenieService()
    {
        return genieService;
    }
}