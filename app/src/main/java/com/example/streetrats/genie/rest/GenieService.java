package com.example.streetrats.genie.rest;


import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

public interface GenieService {

    @Headers("Content-Type: application/json")
    @POST("/api/user")
    void getUser(@Body UserRequest body, Callback<User> callBack);

    @Headers("Content-Type: application/json")
    @GET("/api/user/{access_token}/friends")
    void getFriends(@Path("access_token") String access_token, Callback<List<User>> callback);

    @Headers("Content-Type: application/json")
    @GET("/api/user/{access_token}/products")
    void getMyProducts(@Path("access_token") String access_token, @QueryMap(encodeNames=true) Map<String, String> filters, Callback<List<Product>> callback);

    @Headers("Content-Type: application/json")
    @GET("/api/user/{access_token}/products/bought")
    void getMyBoughtProducts(@Path("access_token") String access_token, @QueryMap(encodeNames=true) Map<String, String> filters, Callback<List<Product>> callback);

    @Headers("Content-Type: application/json")
    @GET("/api/user/{access_token}/friends/products")
    void getFriendsProducts(@Path("access_token") String access_token, @QueryMap(encodeNames=true) Map<String, String> filters, Callback<List<Product>> callback);

    @Headers("Content-Type: application/json")
    @POST("/api/user/products")
    void addProduct(@Body AddProductRequest body, Callback<Product> callback);

    @Headers("Content-Type: application/json")
    @DELETE("/api/user/products/{id}")
    void removeProduct(@Path("id") String id, Callback<Product> callback);

    @Headers("Content-Type: application/json")
    @POST("/api/user/friends/products")
    void buyProduct(@Body BuyProductRequest body, Callback<Product> callback);

    @Headers("Content-Type: application/json")
    @POST("/api/user/friends/products/unbought")
    void unBuyProduct(@Body BuyProductRequest body, Callback<Product> callback);

}