package com.example.streetrats.genie;


import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

public interface GenieService {

    @Headers("Content-Type: application/json")
    @POST("/api/user")
    void getUser(@Body UserRequest body, Callback<User> callBack);
}