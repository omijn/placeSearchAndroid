package com.example.omijn.placeandentertainmentsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SearchFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // get search button and attach event listener
        Button searchButton = (Button) view.findViewById(R.id.button_search);
        searchButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "ANDROID DEVELOPMENT IS SO COOL", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), ResultsActivity.class);
        startActivity(intent);
    }
}
