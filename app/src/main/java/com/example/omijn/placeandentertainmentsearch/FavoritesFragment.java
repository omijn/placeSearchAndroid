package com.example.omijn.placeandentertainmentsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class FavoritesFragment extends Fragment implements ResultsAdapter.ListItemClickListener {

    private SharedPreferences favoritesSharedPreferences;
    private SharedPreferences.Editor favoritesEditor;
    private JSONArray favorites;
    private ArrayList<PlaceResult> favoritesList;

    private ResultsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    private Toast favoritesToast;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        favoritesSharedPreferences = v.getContext().getSharedPreferences(getString(R.string.fav_shared_pref_file), v.getContext().MODE_PRIVATE);
        favoritesEditor = favoritesSharedPreferences.edit();

        try {
            favorites = new JSONArray(favoritesSharedPreferences.getString(getString(R.string.fav_json), ""));
            favoritesList = convertToArrayList(favorites);
        } catch (JSONException e) {
            e.printStackTrace();
            favorites = new JSONArray();
            favoritesList = new ArrayList<>();
        }


        // new adapter
        adapter = new ResultsAdapter(favoritesList, this);

        recyclerView = v.findViewById(R.id.rv_favorites);
        recyclerView.setAdapter(adapter);

        emptyView = v.findViewById(R.id.ra_tv_no_favorites);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if (favoritesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return v;
    }

    public ArrayList<PlaceResult> convertToArrayList(JSONArray array) {
        ArrayList<PlaceResult> arrayList = new ArrayList<>();

        for (int i = 0; i < favorites.length(); i++) {
            JSONObject jObj;
            try {
                jObj = favorites.getJSONObject(i);
                String placeId = jObj.getString("place_id");
                String name = jObj.getString("name");
                String address = jObj.getString("address");
                String icon = jObj.getString("icon");
                arrayList.add(new PlaceResult(name, address, icon, placeId, true));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return arrayList;
    }

    @Override
    public void onListItemDetailsSurfaceClicked(int clickedItemIndex) {
        // show progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching results");
        progressDialog.show();

        final RequestQueue queue = Volley.newRequestQueue(getActivity());

        String detailsUrl = NetworkUtils.buildDetailsUrl(favoritesList.get(clickedItemIndex).getPlace_id());
        final Intent intent = new Intent(getActivity(), DetailsActivity.class);


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
                Toast.makeText(getActivity(), "A network error occurred. Please try again later.", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onListItemFavoriteToggleClicked(int clickedItemIndex) {
        PlaceResult currentResult = favoritesList.get(clickedItemIndex);
        String favoritesToastMsg = null;

        // try to get shared preferences JSON file if it exists.
        try {
            favorites = new JSONArray(favoritesSharedPreferences.getString(getString(R.string.fav_json), ""));
        } catch (JSONException e) {
            e.printStackTrace();
            favorites = new JSONArray();
        }


        // we know that all items in the favorites list are favorites, so we don't need to check like we do in ResultsActivity
        currentResult.setFavorite(false);

        // remove item from sharedpreferences file as well as recyclerview list
        favorites.remove(clickedItemIndex);
        favoritesList.remove(clickedItemIndex);

        favoritesToastMsg = currentResult.getName() + " was removed from favorites.";


        // put JSONArray back into shared preferences file
        favoritesEditor.putString(getString(R.string.fav_json), favorites.toString());

        // save changes
        favoritesEditor.apply();

        // tell adapter to update view
        adapter.notifyDataSetChanged();

        if (favoritesToast != null) {
            favoritesToast.cancel();
        }

        favoritesToast = Toast.makeText(getActivity(), favoritesToastMsg, Toast.LENGTH_SHORT);
        favoritesToast.show();

        if (favoritesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
