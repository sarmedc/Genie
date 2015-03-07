package com.example.streetrats.genie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.streetrats.genie.rest.GenieService;
import com.example.streetrats.genie.rest.Product;
import com.example.streetrats.genie.rest.RestClient;
import com.facebook.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    RestClient restClient;
    GenieService genieService;

    ArrayList<Product> productArray = new ArrayList<Product>();
    ProductsAdapter adapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_landing,
                container, false);

        restClient = new RestClient();
        genieService = restClient.getGenieService();

        /*getUserInfo(view);

        adapter = new ProductsAdapter(getActivity(), productArray);
        ListView theListView = (ListView) view.findViewById(R.id.friendsProductListView);
        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Product product = (Product) adapter.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), ItemDetail.class);
                intent.putExtra("PRODUCT", product);
                intent.putExtra("PARENT_ACTIVITY", "HomeFragment");
                startActivity(intent);
            }
        });

        getFriendsProducts(view, adapter);*/

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ProductsAdapter(getActivity(), productArray);
        mRecyclerView.setAdapter(mAdapter);

        getFriendsProducts(view, mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                facebookLogout();
                break;
        }
        return true;
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

    /*public void getUserInfo(final View view) {
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
                TextView name = (TextView) view.findViewById(R.id.user_name);
                name.setText(user.first_name + " " + user.last_name.charAt(0) + ".");
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println(retrofitError.getMessage());
            }
        });
    }*/


    public void getFriendsProducts(final View view, final RecyclerView.Adapter adapter) {
        if(restClient == null || genieService == null) {
            return;
        }
        genieService.getFriendsProducts(Session.getActiveSession().getAccessToken().toString(), new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                if (products.size() != 0) {
                    productArray.clear();
                    for (int i = 0; i < products.size(); i++) {
                        productArray.add(products.get(i));
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
}

