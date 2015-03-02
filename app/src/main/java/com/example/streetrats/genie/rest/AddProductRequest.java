package com.example.streetrats.genie.rest;

/**
 * Created by saif on 3/1/15.
 */
public class AddProductRequest {

    final String item;
    final String access_token;

    public AddProductRequest(String item, String access_token) {
        this.item = item;
        this.access_token = access_token;
    }

}
