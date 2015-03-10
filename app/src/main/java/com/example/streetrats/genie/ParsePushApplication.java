package com.example.streetrats.genie;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by saif on 3/8/15.
 */
public class ParsePushApplication extends Application {
    @Override
    public void onCreate() {
        Parse.initialize(this, "XU6bUmIRtiSFHlVjnaSjQODfXAb7t1Ruo9RGXzVc", "01AVOUJrn8uTH9AZ1j1U8KD3R6kIRWkGJbYrFvXv");
        PushService.setDefaultPushCallback(this, HomeActivity.class);
        // Save the current installation.
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("channel54fbc31de09f1f030039fb71", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
