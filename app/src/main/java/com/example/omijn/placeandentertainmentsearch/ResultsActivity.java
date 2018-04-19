package com.example.omijn.placeandentertainmentsearch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // create data (or get data from prev intent)
        final ArrayList<PlaceResult> sampleData = new ArrayList<>();
        sampleData.add(new PlaceResult("USC", "Los Angeles"));
        sampleData.add(new PlaceResult("UCLA", "Los Angeles"));
        sampleData.add(new PlaceResult("UC Irvine", "Irvine"));
        sampleData.add(new PlaceResult("UCSD", "San Diego"));
        sampleData.add(new PlaceResult("NCSU", "Raleigh"));
        sampleData.add(new PlaceResult("Virginia Tech", "Blacksburg"));
        sampleData.add(new PlaceResult("USC", "Los Angeles"));
        sampleData.add(new PlaceResult("UCLA", "Los Angeles"));
        sampleData.add(new PlaceResult("UC Irvine", "Irvine"));
        sampleData.add(new PlaceResult("UCSD", "San Diego"));
        sampleData.add(new PlaceResult("NCSU", "Raleigh"));
        sampleData.add(new PlaceResult("Virginia Tech", "Blacksburg"));
        sampleData.add(new PlaceResult("USC", "Los Angeles"));
        sampleData.add(new PlaceResult("UCLA", "Los Angeles"));
        sampleData.add(new PlaceResult("UC Irvine", "Irvine"));
        sampleData.add(new PlaceResult("UCSD", "San Diego"));
        sampleData.add(new PlaceResult("NCSU", "Raleigh"));
        sampleData.add(new PlaceResult("Virginia Tech", "Blacksburg"));

        // new adapter
        PlaceResultAdapter adapter = new PlaceResultAdapter(this, sampleData);

        ListView listView = (ListView) findViewById(R.id.lv_place_results);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ResultsActivity.this, sampleData.get(position).getName(), Toast.LENGTH_SHORT).show();

                // on item click, go to DetailsActivity and pass some data
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, sampleData.get(position).getName());
                startActivity(intent);
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

            return listItemView;
        }


    }
}
