package com.example.omijn.placeandentertainmentsearch;

// TODO: 4/22/18 Remove unused imports
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailsActivity extends AppCompatActivity {

    private DetailsActivityPagerAdapter mDetailsActivityPagerAdapter;
    private ViewPager mViewPager;
    private String textFromResultsActivity;

    // info fragment data
    private String infoFragmentAddress;
    private String infoFragmentPhone;
    private int infoFragmentPrice;
    private double infoFragmentRating;
    private String infoFragmentGooglePage;
    private String infoFragmentWebsite;

    // photos fragment data
    private String placeId;

    // map fragment data
    private Double mapFragmentLat;
    private Double mapFragmentLng;
    private String mapFragmentName;

    // reviews fragment data
    private String reviewsFragmentReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mDetailsActivityPagerAdapter = new DetailsActivityPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.da_vp_container);
        mViewPager.setAdapter(mDetailsActivityPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String textFromResultsActivity = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);

            try {
                JSONObject jsonData = new JSONObject(textFromResultsActivity);
                JSONObject placeDetails = jsonData.getJSONObject("json").getJSONObject("result");

                // extract data for info fragment
                infoFragmentAddress = placeDetails.optString("formatted_address", null);
                infoFragmentPhone = placeDetails.optString("formatted_phone_number", null);
                infoFragmentPrice = placeDetails.optInt("price_level", -1);
                infoFragmentRating = placeDetails.optDouble("rating", -1);
                infoFragmentGooglePage = placeDetails.optString("url", null);
                infoFragmentWebsite = placeDetails.optString("website", null);

                // extract data for photos fragment
                placeId = placeDetails.getString("place_id");

                // extract data for map fragment
                mapFragmentLat = placeDetails.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                mapFragmentLng = placeDetails.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                mapFragmentName = placeDetails.getString("name");

                // set activity label to place name
                getSupportActionBar().setTitle(mapFragmentName);

                // extract data for reviews fragment
                JSONArray reviews = placeDetails.optJSONArray("reviews");
                if (reviews != null) {
                    reviewsFragmentReviews = reviews.toString();
                } else {
                    reviewsFragmentReviews = "";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DetailsActivityPagerAdapter extends FragmentPagerAdapter {

        public DetailsActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                InfoFragment infoFragment = new InfoFragment(); // first tab
                Bundle args = infoFragment.getArguments();
                if (args == null) {
                    args = new Bundle();
                }
                args.putString("address", infoFragmentAddress);
                args.putString("phone", infoFragmentPhone);
                args.putInt("price", infoFragmentPrice);
                args.putDouble("rating", infoFragmentRating);
                args.putString("google_page", infoFragmentGooglePage);
                args.putString("website", infoFragmentWebsite);


                infoFragment.setArguments(args);
                return infoFragment;
            } else if (position == 1) {
                PhotosFragment photosFragment = new PhotosFragment(); // second tab
                Bundle args = photosFragment.getArguments();
                if (args == null) {
                    args = new Bundle();
                }
                args.putString("place_id", placeId);
                photosFragment.setArguments(args);

                return photosFragment;
            } else if (position == 2) {
                MapFragment mapFragment = new MapFragment(); // third tab
                Bundle args = mapFragment.getArguments();
                if (args == null) {
                    args = new Bundle();
                }
                args.putDouble("lat", mapFragmentLat);
                args.putDouble("lng", mapFragmentLng);
                args.putString("name", mapFragmentName);


                mapFragment.setArguments(args);

                return mapFragment;
            } else {
                ReviewsFragment reviewsFragment = new ReviewsFragment(); // fourth tab

                Bundle args = reviewsFragment.getArguments();
                if (args == null) {
                    args = new Bundle();
                }
                args.putString("reviews", reviewsFragmentReviews);

                reviewsFragment.setArguments(args);


                return reviewsFragment;
            }

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
