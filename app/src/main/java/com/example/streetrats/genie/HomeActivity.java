package com.example.streetrats.genie;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.streetrats.genie.rest.GenieService;
import com.example.streetrats.genie.rest.RestClient;
import com.example.streetrats.genie.rest.User;
import com.example.streetrats.genie.rest.UserRequest;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.parse.Parse;
import com.parse.PushService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener, android.support.v7.app.ActionBar.TabListener {

    private static final String TAG = "HomeActivity";

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    private UiLifecycleHelper uiHelper;

    private boolean isResumed = false;

    ViewPager mViewPager;

    RestClient restClient;
    GenieService genieService;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        Parse.initialize(this, "XU6bUmIRtiSFHlVjnaSjQODfXAb7t1Ruo9RGXzVc", "01AVOUJrn8uTH9AZ1j1U8KD3R6kIRWkGJbYrFvXv");
        PushService.setDefaultPushCallback(this, HomeActivity.class);

        Session session = Session.getActiveSession();

        Log.d(TAG, session.getAccessToken());

        if (Session.getActiveSession() != null) {
            Log.d(TAG, "SESSION IS NOT NULL");
            if (!session.isClosed()) {
                Log.d(TAG, "SESSION IS OPEN");
            }
            else {
                Log.d(TAG, "SESSION IS CLOSED");
            }
        }
        else {
            Log.d(TAG, "SESSION IS NULL");
        }

        restClient = new RestClient();
        genieService = restClient.getGenieService();

        View view = this.findViewById(android.R.id.content);

        getUserInfo(view);

        setContentView(R.layout.home_activity);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));


        // Specify that we will be displaying tabs in the action bar.
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                getSupportActionBar().setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            getSupportActionBar().addTab(
                    getSupportActionBar().newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        /*set tab colors here*/
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_blue)));

    }

    public void getUserInfo(final View view) {
        if(restClient == null || genieService == null) {
            return;
        }
        genieService.getUser(new UserRequest(Session.getActiveSession().getAccessToken()), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                System.out.println("User Name: " + user.first_name + " " + user.last_name);
                System.out.println("User ID: " + user._id);
                System.out.println("User FB ID: " + user.fb_id);
                for(int i = 0; i < user.friends.size(); i++) {
                    System.out.println("User Friend: " + user.friends.get(i));
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println(retrofitError.getMessage());
            }
        });

    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (isResumed) {
            if (session == null || state.isClosed()) {
                // If the session state is closed:
                // Show the authenticated fragment
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("fb_session", session);
                startActivity(i);
                finish();
            } else if (state.isOpened()) {
                // If the session state is open:
            }
        }
    }

    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                    // checkLoggedIn();
                }
            };

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
        if (Session.getActiveSession() == null || Session.getActiveSession().isClosed()){
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
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

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            Bundle args;
            switch (i) {
                case 0:
                    // The other sections of the app are dummy placeholders.
                    return new HomeFragment();
                    /*fragment = new DummySectionFragment();
                    args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;*/
                case 1:
                    return new FriendFragment();
                case 2:
                    return new ProfileFragment();
                default:
                    // The other sections of the app are dummy placeholders.
                    fragment = new DummySectionFragment();
                    args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return "Section " + (position + 1);
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Friends";
                case 2:
                    return "Profile";
                default:
                    return "";
            }
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
