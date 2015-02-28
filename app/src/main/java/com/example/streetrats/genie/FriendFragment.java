package com.example.streetrats.genie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.streetrats.genie.rest.GenieService;
import com.example.streetrats.genie.rest.RestClient;
import com.example.streetrats.genie.rest.User;
import com.facebook.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FriendFragment extends Fragment {

    private static final String TAG = "FriendFragment";

    RestClient restClient;
    GenieService genieService;

    /*private final String[] items = { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };*/

    ArrayList<User> userArray = new ArrayList<User>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_browsefriends,
                container, false);

        restClient = new RestClient();
        genieService = restClient.getGenieService();

        /*ListAdapter theAdapter = new MyAdapter(getActivity(), items);*/
        UsersAdapter adapter = new UsersAdapter(getActivity(), userArray);

        ListView theListView = (ListView) view.findViewById(R.id.friendProductListView);

        theListView.setAdapter(adapter);

        getFriends(view, adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_logout:
                facebookLogout();
                break;
        }
        return true;
    }

    public void getFriends(final View view, final UsersAdapter adapter) {
        if(restClient == null || genieService == null) {
            return;
        }
        genieService.getFriends(Session.getActiveSession().getAccessToken().toString(), new Callback<List<User>>() {
            @Override
            public void success(List<User> friends, Response response) {
                if(friends.size() != 0) {
                    userArray.clear();
                    for (int i = 0; i < friends.size(); i++) {
                        userArray.add(friends.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println(retrofitError.getMessage());
            }
        });

    }

    public void facebookLogout() {
        Session session = Session.getActiveSession();
        if (session != null) {
            Log.d(TAG, "SESSION IS NOT NULL");
            if (!session.isClosed()) {
                Log.d(TAG, "SESSION IS OPEN");
                session.closeAndClearTokenInformation();
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(i);
                getActivity().finish();
            }
            else {
                Log.d(TAG, "SESSION IS CLOSED");
            }
        }
        else {
            Log.d(TAG, "SESSION IS NULL");
        }
    }
}

