package com.example.streetrats.genie;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

/**
 * Created by saif on 3/8/15.
 */
public class ParsePushApplication extends Application {

    @Override
    public void onCreate() {
        Parse.initialize(this, "XU6bUmIRtiSFHlVjnaSjQODfXAb7t1Ruo9RGXzVc", "01AVOUJrn8uTH9AZ1j1U8KD3R6kIRWkGJbYrFvXv");
        PushService.setDefaultPushCallback(this, HomeActivity.class);
    }
}
