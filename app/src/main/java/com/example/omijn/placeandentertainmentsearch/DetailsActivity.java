package com.example.omijn.placeandentertainmentsearch;

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


public class DetailsActivity extends AppCompatActivity {

    private DetailsActivityPagerAdapter mDetailsActivityPagerAdapter;
    private ViewPager mViewPager;

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

            Toast.makeText(this, textFromResultsActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public class DetailsActivityPagerAdapter extends FragmentPagerAdapter {

        public DetailsActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new InfoFragment(); // first tab
            else if (position == 1)
                return new PhotosFragment(); // second tab
            else if (position == 2)
                return new MapFragment(); // third tab
            else
                return new ReviewsFragment(); // fourth tab

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
