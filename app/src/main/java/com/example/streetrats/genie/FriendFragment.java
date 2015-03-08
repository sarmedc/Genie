package com.example.streetrats.genie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
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

    private static final int RESULT_OK = 1;
    private static final int RESULT_CANCELED = 0;

    RestClient restClient;
    GenieService genieService;

    ArrayList<User> userArray = new ArrayList<User>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends,
                container, false);

        restClient = new RestClient();
        genieService = restClient.getGenieService();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UsersAdapter(userArray);
        mRecyclerView.setAdapter(mAdapter);

        getFriends(view, mAdapter);

        return view;
    }

    public void getFriends(final View view, final RecyclerView.Adapter adapter) {
        if(restClient == null || genieService == null) {
            return;
        }

        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Fetching Your Friends")
                .content("Please Wait..")
                .progress(true, 0)
                .show();

        genieService.getFriends(Session.getActiveSession().getAccessToken().toString(), new Callback<List<User>>() {
            @Override
            public void success(List<User> friends, Response response) {
                dialog.cancel();
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
                dialog.cancel();
                System.out.println(retrofitError.getMessage());
            }
        });

    }
}

