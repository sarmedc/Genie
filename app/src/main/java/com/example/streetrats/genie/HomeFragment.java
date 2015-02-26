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

import com.facebook.Session;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_landing,
                container, false);

        //ListAdapter theAdapter = new MyAdapter(getActivity(), items);

       //ListView theListView = (ListView) view.findViewById(R.id.listView);

        //theListView.setAdapter(theAdapter);

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

