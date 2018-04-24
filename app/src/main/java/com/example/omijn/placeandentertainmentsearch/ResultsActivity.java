package com.example.omijn.placeandentertainmentsearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

import okhttp3.internal.Util;

public class ResultsActivity extends AppCompatActivity implements ResultsAdapter.ListItemClickListener {

    private ArrayList<PlaceResult> listData;
    private ResultsAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private Toast favoritesToast;

    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONArray favorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        listData = new ArrayList<>();

        favoritesSharedPreferences = getSharedPreferences(getString(R.string.fav_shared_pref_file), MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();

        try {
            favorites = new JSONArray(favoritesSharedPreferences.getString(getString(R.string.fav_json), ""));
        } catch (JSONException e) {
            e.printStackTrace();
            favorites = new JSONArray();
        }

        // get data from MainActivity
        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String jsonData = callingIntent.getStringExtra(Intent.EXTRA_TEXT);

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject res = resultsArray.getJSONObject(i);
                    String address = res.getString("address");
                    String coords = res.getString("coords");
                    String icon = res.getString("icon");
                    String name = res.getString("name");
                    String place_id = res.getString("place_id");

                    Utils.FavoriteCheckResult favChkRes = Utils.isFavorite(place_id, favorites);

                    listData.add(new PlaceResult(name, address, icon, place_id, favChkRes.isFavorite()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // new adapter
        adapter = new ResultsAdapter(listData, this);

        recyclerView = findViewById(R.id.rv_place_results);
        recyclerView.setAdapter(adapter);

        TextView emptyView = findViewById(R.id.ra_tv_no_results);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (listData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onListItemDetailsSurfaceClicked(int clickedItemIndex) {
        // show progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching results");
        progressDialog.show();

        final RequestQueue queue = Volley.newRequestQueue(this);

        String detailsUrl = NetworkUtils.buildDetailsUrl(listData.get(clickedItemIndex).getPlace_id());
        final Intent intent = new Intent(this, DetailsActivity.class);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, detailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();

                intent.putExtra(Intent.EXTRA_TEXT, response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "A network error occurred. Please try again later.", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onListItemFavoriteToggleClicked(int clickedItemIndex) {
        PlaceResult currentResult = listData.get(clickedItemIndex);
        String favoritesToastMsg = null;

        // try to get shared preferences JSON file if it exists.
        try {
            favorites = new JSONArray(favoritesSharedPreferences.getString(getString(R.string.fav_json), ""));
        } catch (JSONException e) {
            e.printStackTrace();
            favorites = new JSONArray();
        }

        Utils.FavoriteCheckResult favCheckResult = Utils.isFavorite(currentResult, favorites);

        // toggle favorites state
        // if selected item is currently a favorite item, un-favorite it (remove from sharedpreferences)
        if (favCheckResult.isFavorite()) {
            currentResult.setFavorite(false);
            favorites.remove(favCheckResult.getFavoritePosition());
            favoritesToastMsg = currentResult.getName() + " was removed from favorites.";
        } else {    // else, favorite it (add to sharedpreferences)

            currentResult.setFavorite(true);

            // create JSONObject containing data of new favorite
            JSONObject newFavorite = new JSONObject();
            try {
                newFavorite.put("place_id", currentResult.getPlace_id());
                newFavorite.put("name", currentResult.getName());
                newFavorite.put("address", currentResult.getAddress());
                newFavorite.put("icon", currentResult.getIcon());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // insert new favorite into JSONArray
            favorites.put(newFavorite);

            // create toast message
            favoritesToastMsg = currentResult.getName() + " was added to favorites.";
        }

        // put JSONArray back into shared preferences file
        favoritesEditor.putString(getString(R.string.fav_json), favorites.toString());

        // save changes
        favoritesEditor.apply();

        // tell adapter to update view
        adapter.notifyItemChanged(clickedItemIndex);

        if (favoritesToast != null) {
            favoritesToast.cancel();
        }

        favoritesToast = Toast.makeText(this, favoritesToastMsg, Toast.LENGTH_SHORT);
        favoritesToast.show();
    }

}
