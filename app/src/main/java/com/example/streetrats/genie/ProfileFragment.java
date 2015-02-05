package com.example.streetrats.genie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by saif on 2/5/2015.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile,
                container, false);
        return view;
    }

}
