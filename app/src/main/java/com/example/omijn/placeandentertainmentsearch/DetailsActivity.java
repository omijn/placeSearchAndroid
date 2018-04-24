package com.example.omijn.placeandentertainmentsearch;

// TODO: 4/22/18 Remove unused imports
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailsActivity extends AppCompatActivity {

    private DetailsActivityPagerAdapter mDetailsActivityPagerAdapter;
    private ViewPager mViewPager;

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

    // twitter
    private final static String twitterHashtags = "TravelAndEntertainmentSearch";
    private String twitterText;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.da_action_tweet:
                openTweetLink();
                break;
            case R.id.da_action_favorite:
//                toggleFavorite();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTwitterText() {
        twitterText = "Check out " + mapFragmentName + " located at " + infoFragmentAddress + ". Website: " + infoFragmentWebsite;
    }

    public void openTweetLink() {
        String twitterLink = "https://twitter.com/intent/tweet?text=" + twitterText + "&hashtags=" + twitterHashtags;
        Uri uri = Uri.parse(twitterLink);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // TODO: 4/23/18 Remove ActionBar shadow

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

                // set Twitter link text
                setTwitterText();

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
