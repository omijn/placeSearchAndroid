package com.example.omijn.placeandentertainmentsearch;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements ReviewsAdapter.ListItemClickListener {

    private ArrayList<Review> reviewsData;
    private ReviewsAdapter adapter;

    private Spinner sortSpinner;

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

                    reviewsData.add(new Review(i, reviewText, authorName, reviewLink, authorPhotoLink, timestamp, rating));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = v.findViewById(R.id.da_rf_rv_reviews);
        TextView emptyView = v.findViewById(R.id.da_rf_tv_no_reviews);
        sortSpinner = v.findViewById(R.id.da_rf_sp_sort);

        adapter = new ReviewsAdapter(reviewsData, this);

        recyclerView.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        defaultSort();
                        break;
                    case 1:
                        rateSort(-1);   // descending (highest rating first)
                        break;
                    case 2:
                        rateSort(1);    // ascending (lowest rating first)
                        break;
                    case 3:
                        dateSort(-1);   // descending (newest review first)
                        break;
                    case 4:
                        dateSort(1);    // ascending (oldest review first)
                        break;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Uri reviewsPage = Uri.parse(reviewsData.get(clickedItemIndex).getReviewLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, reviewsPage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public void rateSort(final int order) {
        Collections.sort(reviewsData, new Comparator<Review>() {
            @Override
            public int compare(Review r1, Review r2) {
                if (r1.getRating() > r2.getRating()) {
                    return order;
                } else if (r1.getRating() < r2.getRating()) {
                    return -1 * order;
                } else {
                    return 0;
                }
            }
        });
    }

    public void dateSort(final int order) {
        Collections.sort(reviewsData, new Comparator<Review>() {
            @Override
            public int compare(Review r1, Review r2) {
                if (r1.getUnixTimestamp() > r2.getUnixTimestamp()) {
                    return order;
                } else if (r1.getUnixTimestamp() < r2.getUnixTimestamp()) {
                    return -1 * order;
                } else {
                    return 0;
                }
            }
        });
    }

    public void defaultSort() {
        Collections.sort(reviewsData, new Comparator<Review>() {
            @Override
            public int compare(Review r1, Review r2) {
                if (r1.getIndex() > r2.getIndex()) {
                    return 1;
                } else if (r1.getIndex() < r2.getIndex()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

}
