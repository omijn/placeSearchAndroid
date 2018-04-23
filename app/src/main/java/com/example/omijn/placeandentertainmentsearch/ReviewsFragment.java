package com.example.omijn.placeandentertainmentsearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    private ArrayList<Review> reviewsData;
    private ReviewsAdapter adapter;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reviews, container, false);

        String reviewsJson = getArguments().getString("reviews");

        reviewsData = new ArrayList<>();
        try {
            if (reviewsJson != null && !reviewsJson.equals("")) {
                JSONArray reviews = new JSONArray(reviewsJson);
                for (int i = 0; i < reviews.length(); i++) {
                    JSONObject review = reviews.getJSONObject(i);
                    String authorName = review.optString("author_name", "A Google user.");
                    String authorPhotoLink = review.optString("profile_photo_url", "");
                    Double rating = review.optDouble("rating", -1);
                    String timestamp = review.optString("time", "");
                    String reviewText = review.optString("text", "");
                    String reviewLink = review.optString("author_url", "");

                    reviewsData.add(new Review(reviewText, authorName, reviewLink, authorPhotoLink, timestamp, rating));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = v.findViewById(R.id.da_rf_rv_reviews);
        TextView emptyView = v.findViewById(R.id.da_rf_tv_no_reviews);

        adapter = new ReviewsAdapter(reviewsData);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (reviewsData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return v;

    }

}
