package com.example.omijn.placeandentertainmentsearch;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ResultsActivity extends AppCompatActivity {

    private ArrayList<PlaceResult> listData;
    private PlaceResultAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        listData = new ArrayList<>();

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
                    listData.add(new PlaceResult(name, address, icon, place_id, coords));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // TODO: 4/22/18 Change this to a RecyclerView

        // new adapter
        adapter = new PlaceResultAdapter(this, listData);

        listView = (ListView) findViewById(R.id.lv_place_results);
        listView.setAdapter(adapter);

        final RequestQueue queue = Volley.newRequestQueue(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//                Toast.makeText(ResultsActivity.this, listData.get(position).getName(), Toast.LENGTH_SHORT).show();

                String detailsUrl = NetworkUtils.buildDetailsUrl(listData.get(position).getPlace_id());
                final Intent intent = new Intent(view.getContext(), DetailsActivity.class);


                StringRequest stringRequest = new StringRequest(Request.Method.GET, detailsUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        intent.putExtra(Intent.EXTRA_TEXT, response);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "A network error occurred.", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(stringRequest);
            }
        });
    }

    public class PlaceResultAdapter extends ArrayAdapter<PlaceResult> {

        public PlaceResultAdapter(Activity context, ArrayList<PlaceResult> data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.place_result_list_item, parent, false);
            }

            PlaceResult currentPlaceResult = getItem(position);

            // fill name
            TextView nameTextView = listItemView.findViewById(R.id.tv_place_result_name);
            nameTextView.setText(currentPlaceResult.getName());

            // fill address
            TextView addressTextView = listItemView.findViewById(R.id.tv_place_result_address);
            addressTextView.setText(currentPlaceResult.getAddress());

            // fill icon
            ImageView iconImageView = listItemView.findViewById(R.id.iv_place_result_icon);
            Picasso.get().load(currentPlaceResult.getIcon()).into(iconImageView);

            return listItemView;
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        adapter.notifyDataSetChanged();
//    }
}
