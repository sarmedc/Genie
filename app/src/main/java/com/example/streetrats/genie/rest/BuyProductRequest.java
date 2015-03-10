package com.example.streetrats.genie.rest;

/**
 * Created by saif on 3/1/15.
 */
public class BuyProductRequest {

    final String product_id;
    final String access_token;

    public BuyProductRequest(String product_id, String access_token) {
        this.product_id = product_id;
        this.access_token = access_token;
    }

}
