package com.example.omijn.placeandentertainmentsearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private LinearLayout addressRow;
    private LinearLayout phoneRow;
    private LinearLayout priceRow;
    private LinearLayout ratingRow;
    private LinearLayout googlePageRow;
    private LinearLayout websiteRow;

    private TextView addressTextView;
    private TextView phoneTextView;
    private TextView priceTextView;
    private TextView ratingTextView;
    private TextView googlePageTextView;
    private TextView websiteTextView;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_info, container, false);

        String address = getArguments().getString("address");
        String phone = getArguments().getString("phone");
        int price = getArguments().getInt("price");
        Double rating = getArguments().getDouble("rating");
        String googlePage = getArguments().getString("google_page");
        String website = getArguments().getString("website");

        if (address != null) {
            addressRow = v.findViewById(R.id.da_if_ll_address);
            addressRow.setVisibility(View.VISIBLE);
            addressTextView = v.findViewById(R.id.da_tv_info_address);
            addressTextView.setText(address);
        }

        if (phone != null) {
            phoneRow = v.findViewById(R.id.da_if_ll_phone);
            phoneRow.setVisibility(View.VISIBLE);
            phoneTextView = v.findViewById(R.id.da_tv_info_phone);
            phoneTextView.setText(phone);
        }

        if (price != -1) {
            priceRow = v.findViewById(R.id.da_if_ll_price);
            priceRow.setVisibility(View.VISIBLE);
            priceTextView = v.findViewById(R.id.da_tv_info_price);
            priceTextView.setText(new String(new char[price]).replace("\0", "$")); // repeat "$" sign "price" number of times
        }

        // TODO: 4/22/18 Use RatingBar
        if (rating != -1) {
            ratingRow = v.findViewById(R.id.da_if_ll_rating);
            ratingRow.setVisibility(View.VISIBLE);
            ratingTextView = v.findViewById(R.id.da_tv_info_rating);
            ratingTextView.setText(Double.toString(rating));
        }

        if (googlePage != null) {
            googlePageRow = v.findViewById(R.id.da_if_ll_google_page);
            googlePageRow.setVisibility(View.VISIBLE);
            googlePageTextView = v.findViewById(R.id.da_tv_info_google_page);
            googlePageTextView.setText(googlePage);
        }

        if (website != null) {
            websiteRow = v.findViewById(R.id.da_if_ll_website);
            websiteRow.setVisibility(View.VISIBLE);
            websiteTextView = v.findViewById(R.id.da_tv_info_website);
            websiteTextView.setText(website);
        }

        return v;

    }

}
