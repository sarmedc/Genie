package com.example.streetrats.genie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private UiLifecycleHelper uiHelper;

    private boolean isResumed = false;

    /*private static final int LOGIN = 0;
    private static final int PROFILE = 1;
    private static final int ITEM = 2;
    private static final int ADD_ITEM = 3;
    private static final int ADD_METHOD = 4;
    private static final int FRAGMENT_COUNT = ADD_METHOD + 1;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        checkLoggedIn();

        setContentView(R.layout.login);

        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("user_friends"));


        /*FragmentManager fm = getSupportFragmentManager();
        fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
        fragments[PROFILE] = fm.findFragmentById(R.id.profileFragment);
        fragments[ITEM] = fm.findFragmentById(R.id.itemFragment);
        fragments[ADD_ITEM] = fm.findFragmentById(R.id.addItemFragment);
        fragments[ADD_METHOD] = fm.findFragmentById(R.id.addMethodFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();*/
    }

    /*private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }*/

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (isResumed) {
            if (state.isOpened()) {
                // If the session state is open:
                // Show the authenticated fragment
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            } else if (state.isClosed()) {
                // If the session state is closed:
                // Show the login fragment
            }
        }
    }

    /*@Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            // if the session is already open,
            // try to show the selection fragment
            showFragment(PROFILE, false);
        } else {
            // otherwise present the splash screen
            // and ask the person to login.
            showFragment(LOGIN, false);
        }
    }*/

    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                    // checkLoggedIn();
                }
            };

    public void checkLoggedIn() {
        Session session = Session.getActiveSession();

        if (Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else {
            Log.d(TAG, "SESSION IS NULL OR CLOSED");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getActiveSession() != null || Session.getActiveSession().isOpened()){
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            //finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}



/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */