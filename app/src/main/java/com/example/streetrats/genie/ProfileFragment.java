package com.example.streetrats.genie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.streetrats.genie.rest.AddProductRequest;
import com.example.streetrats.genie.rest.GenieService;
import com.example.streetrats.genie.rest.Product;
import com.example.streetrats.genie.rest.RestClient;
import com.facebook.Session;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private static final int RESULT_OK = 1;
    private static final int RESULT_CANCELED = 0;

    RestClient restClient;
    GenieService genieService;

    ArrayList<Product> productArray = new ArrayList<Product>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile,
                container, false);

        restClient = new RestClient();
        genieService = restClient.getGenieService();

        Button scanButton = (Button) view.findViewById(R.id.btnScan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SimpleScannerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button logoutButton = (Button) view.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                facebookLogout();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProductsAdapter(getActivity(), productArray);
        mRecyclerView.setAdapter(mAdapter);

        getMyProducts(view, mAdapter);

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

    public void getMyProducts(final View view, final RecyclerView.Adapter adapter) {
        if(restClient == null || genieService == null) {
            return;
        }

        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Fetching Your Products")
                .content("Please Wait..")
                .progress(true, 0)
                .show();

        genieService.getMyProducts(Session.getActiveSession().getAccessToken().toString(), new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                dialog.cancel();
                Log.d(TAG, "" + products.size());
                if (products.size() != 0) {
                    productArray.clear();
                    for (int i = 0; i < products.size(); i++) {
                        System.out.println(products.get(i).features);
                        productArray.add(products.get(i));
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

    public void addProduct(final String upc) {
        if(restClient == null || genieService == null) {
            return;
        }

        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Adding Your Item")
                .content("Please Wait..")
                .progress(true, 0)
                .show();

        genieService.addProduct(new AddProductRequest(upc, Session.getActiveSession().getAccessToken()), new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                dialog.cancel();
                productArray.add(product);
                mAdapter.notifyDataSetChanged();
                SnackbarManager.show(
                        Snackbar.with(getActivity()) // context
                                .type(SnackbarType.MULTI_LINE) // Set is as a multi-line snackbar
                                .text("Your Item Was Added") // text to be displayed
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT) // make it shorter
                                .animation(false) // don't animate it
                        , getActivity()); // where it is displayed
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                dialog.cancel();
                System.out.println(retrofitError.getMessage());
                Toast toast = Toast.makeText(getActivity(), "This item is not available to add",  Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String upc = data.getStringExtra("upc");
                Log.d(TAG, "UPC: " + upc);
                addProduct(upc);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d(TAG, "NO RESULT RECEIVED");
                Toast toast = Toast.makeText(getActivity(), "No Result Was Recieved",  Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }//onActivityResult

}
