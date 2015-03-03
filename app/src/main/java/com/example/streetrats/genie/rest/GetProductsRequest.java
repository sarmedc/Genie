package com.example.streetrats.genie.rest;

/**
 * Created by saif on 2/28/15.
 */
public class GetProductsRequest {
    final String access_token;

        GetProductsRequest(String access_token) {
            this.access_token = access_token;
        }
}
