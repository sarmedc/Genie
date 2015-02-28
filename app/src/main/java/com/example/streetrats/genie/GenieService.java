package com.example.streetrats.genie;


import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

public interface GenieService {

    @Headers("Content-Type: application/json")
    @POST("/api/user")
    void getUser(@Body UserRequest body, Callback<User> callBack);

    @Headers("Content-Type: application/json")
    @GET("/api/user/{access_token}/friends")
    void getFriends(@Path("access_token") String access_token, Callback<List<User>> callback);

}