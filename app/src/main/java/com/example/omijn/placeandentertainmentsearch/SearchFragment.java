package com.example.omijn.placeandentertainmentsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;



public class SearchFragment extends Fragment implements View.OnClickListener{
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // get search button and attach event listener
        Button searchButton = view.findViewById(R.id.ma_sf_button_search);
        searchButton.setOnClickListener(this);

        return view;
    }

    /*
     * When the Search button is clicked:
     * - Validate form
     * - Build URL
     * - Get data from API
     * - Change activity
     */
    @Override
    public void onClick(View v) {


        TextView keywordTextView = getActivity().findViewById(R.id.ma_sf_et_keyword);
        String keyword = keywordTextView.getText().toString();

        Spinner categorySpinner = getActivity().findViewById(R.id.ma_sf_sp_category);
        String category = categorySpinner.getSelectedItem().toString();

        TextView distanceTextView = getActivity().findViewById(R.id.ma_sf_et_distance);
        String distance = distanceTextView.getText().toString();

        RadioButton radioButton1 = getActivity().findViewById(R.id.ma_sf_radio1);
        RadioButton radioButton2 = getActivity().findViewById(R.id.ma_sf_radio2);

        EditText radio2Location = getActivity().findViewById(R.id.ma_sf_et_radio2_location);

        String locationType = null;
        String location = null;

        if (radioButton1.isChecked()) {
            locationType = "coords";
            location = "34.0266,-118.2831";
//            location = getCurrentLocation();
        } else {
            locationType = "address";
            location = radio2Location.getText().toString();
        }


        // TODO: 4/22/18 validate form


        // show progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching results");
        progressDialog.show();

        // validate input form data

        // build URL using form data
        URL url = NetworkUtils.buildUrl(keyword, category, distance, locationType, location);

        new GetWebDataTask().execute(url);

    }

    // TODO: 4/22/18 Convert to Volley request

    public class GetWebDataTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];

            // make HTTP request
            String jsonData = null;
            try {
                jsonData = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                // success toast
                Toast.makeText(getActivity(), "Successfully completed AsyncTask.", Toast.LENGTH_SHORT).show();

                progressDialog.cancel();

                // start new activity and pass JSON string result through intent
                Intent intent = new Intent(getActivity(), ResultsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, s);
                startActivity(intent);
            }
        }
    }

}
