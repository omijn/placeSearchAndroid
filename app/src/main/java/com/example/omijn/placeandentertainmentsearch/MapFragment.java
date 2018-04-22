package com.example.omijn.placeandentertainmentsearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private static final String TAG = "MapFragment";

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.da_mf_map);

        // https://stackoverflow.com/a/32091722/5422888
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.da_mf_map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map ready");
        Double destinationLocationLat = getArguments().getDouble("lat");
        Double destinationLocationLng = getArguments().getDouble("lng");
        String destinationName = getArguments().getString("name");
        LatLng destinationLocation = new LatLng(destinationLocationLat, destinationLocationLng);

        googleMap.addMarker(new MarkerOptions().position(destinationLocation).title(destinationName));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLocation));
    }
}
