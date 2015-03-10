package com.example.streetrats.genie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.streetrats.genie.rest.GenieService;
import com.example.streetrats.genie.rest.Product;
import com.example.streetrats.genie.rest.RestClient;
import com.example.streetrats.genie.rest.User;
import com.facebook.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BoughtFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "BoughtFragment";

    private View view;

    private static final int RESULT_OK = 1;
    private static final int RESULT_CANCELED = 0;

    RestClient restClient;
    GenieService genieService;

    ArrayList<Product> productArray = new ArrayList<Product>();
    ArrayList<User> userArray = new ArrayList<User>();

    private SwipeRefreshLayout mSwipeLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Map<String, String> filter = new HashMap<String, String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bought,
                container, false);

        restClient = new RestClient();
        genieService = restClient.getGenieService();

        getFriends(view);

        Button filterButton = (Button) view.findViewById(R.id.btnFilter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = new CharSequence[userArray.size()];
                for(int i = 0; i < userArray.size(); i++) {
                    items[i] = userArray.get(i).first_name + " " + userArray.get(i).last_name;
                }
                new MaterialDialog.Builder(getActivity())
                        .title("Filter By Friends")
                        .items(items)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMulti() {
                            @Override
                            public void onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                Map<String, String> filter = new HashMap<String, String>();

                                for(int i = 0; i < which.length; i++) {
                                    try {
                                        System.out.println(userArray.get(which[i]).first_name);
                                        filter.put("owner" + i, userArray.get(which[i])._id);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                getMyBoughtProducts(view, filter);
                            }
                        })
                        .positiveText(R.string.filter)
                        .show();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProductsBoughtAdapter(getActivity(), productArray);
        mRecyclerView.setAdapter(mAdapter);

        getMyBoughtProducts(view, null);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
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

    public void getMyBoughtProducts(final View view, Map<String, String> filter) {
        if(restClient == null || genieService == null) {
            return;
        }

        final RecyclerView.Adapter adapter = mAdapter;

        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Fetching Your Bought Products")
                .content("Please Wait..")
                .progress(true, 0)
                .show();

        genieService.getMyBoughtProducts(Session.getActiveSession().getAccessToken().toString(), filter, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                dialog.cancel();
                Log.d(TAG, "" + products.size());
                productArray.clear();
                for (int i = 0; i < products.size(); i++) {
                    System.out.println(products.get(i).features);
                    productArray.add(products.get(i));
                }
                adapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                dialog.cancel();
                System.out.println(retrofitError.getMessage());
                mSwipeLayout.setRefreshing(false);
            }
        });

    }

    public void getFriends(final View view) {
        if(restClient == null || genieService == null) {
            return;
        }

        /*final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Fetching Your Friends")
                .content("Please Wait..")
                .progress(true, 0)
                .show();*/

        genieService.getFriends(Session.getActiveSession().getAccessToken().toString(), new Callback<List<User>>() {
            @Override
            public void success(List<User> friends, Response response) {
                //dialog.cancel();
                userArray.clear();
                for (int i = 0; i < friends.size(); i++) {
                    userArray.add(friends.get(i));
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                //dialog.cancel();
                System.out.println(retrofitError.getMessage());
            }
        });

    }


    @Override
    public void onRefresh() {
        getMyBoughtProducts(view, null);
    }
}
