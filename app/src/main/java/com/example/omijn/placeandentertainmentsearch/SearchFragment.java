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


public class SearchFragment extends Fragment implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText keywordEditText;
    private Spinner categorySpinner;
    private EditText distanceEditText;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private EditText radio2LocationEditText;

    private TextView errorKeywordTextView;
    private TextView errorRadioLocationTextView;

    private Button searchButton;
    private Button clearButton;

    private String keyword;
    private String category;
    private String distance;
    private String radio2Location;
    private String locationType;
    private String location;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // get references to views
        keywordEditText = view.findViewById(R.id.ma_sf_et_keyword);
        categorySpinner = view.findViewById(R.id.ma_sf_sp_category);
        distanceEditText = view.findViewById(R.id.ma_sf_et_distance);
        radioButton1 = view.findViewById(R.id.ma_sf_radio1);
        radioButton2 = view.findViewById(R.id.ma_sf_radio2);
        radio2LocationEditText = view.findViewById(R.id.ma_sf_et_radio2_location);
        errorKeywordTextView = view.findViewById(R.id.ma_sf_error_keyword);
        errorRadioLocationTextView = view.findViewById(R.id.ma_sf_error_radio2location);

        // attach event listeners to search and clear buttons
        searchButton = view.findViewById(R.id.ma_sf_button_search);
        searchButton.setOnClickListener(this);
        clearButton = view.findViewById(R.id.ma_sf_button_clear);
        clearButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // search button clicked
            case R.id.ma_sf_button_search:

                performSearch();
                break;

            // clear button clicked
            case R.id.ma_sf_button_clear:
                resetForm();
                break;
        }
    }

    public void performSearch() {
        clearErrorMessages();

        // get user inputs
        keyword = keywordEditText.getText().toString();
        category = categorySpinner.getSelectedItem().toString();
        distance = distanceEditText.getText().toString();
        radio2Location = radio2LocationEditText.getText().toString();

        locationType = null;
        location = null;

        if (radioButton1.isChecked()) {
            locationType = "coords";
            location = "34.0266,-118.2831";
//            location = getCurrentLocation();
        } else {
            locationType = "address";
            location = radio2Location;
        }

        // if form is valid, perform search
        if (isFormValid()) {

            // show progress dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching results");
            progressDialog.show();

            // build URL using form data
            URL url = NetworkUtils.buildUrl(keyword, category, distance, locationType, location);

            new GetWebDataTask().execute(url);
        }
    }

    public void resetForm() {
        clearErrorMessages();
        resetInputs();
    }

    public void resetInputs() {
        keywordEditText.setText("");
        categorySpinner.setSelection(0);
        distanceEditText.setText("");
        radioButton1.setChecked(true);
        radio2LocationEditText.setText("");
    }

    public void clearErrorMessages() {
        errorKeywordTextView.setVisibility(View.GONE);
        errorRadioLocationTextView.setVisibility(View.GONE);
    }

    public boolean isFormValid() {
        boolean validity = true;

        if (keyword == null || keyword.equals("")) {
            errorKeywordTextView.setVisibility(View.VISIBLE);
            validity = false;
        }

        if (radioButton2.isChecked() && (radio2Location.equals("") || radio2Location == null)) {
            errorRadioLocationTextView.setVisibility(View.VISIBLE);
            validity = false;
        }

        if (distance == null || distance.equals("")) {
            distance = "10";
        }

        return validity;
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
