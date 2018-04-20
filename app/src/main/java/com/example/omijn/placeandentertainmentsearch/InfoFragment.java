package com.example.omijn.placeandentertainmentsearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    // TODO: 4/20/18 1) Declare Views

    public InfoFragment() {
        // Required empty public constructor
    }

    // TODO: 4/20/18 2) Create constructor to take in values to be populated in views


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
        // TODO: 4/20/18 3) on inflated view, use findViewById to get references to views
        // TODO: 4/20/18 4) populate views with data
    }

}
