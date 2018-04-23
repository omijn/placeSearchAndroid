package com.example.omijn.placeandentertainmentsearch;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.net.URL;


public class SearchFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SearchFragment";

    private ProgressDialog progressDialog;
    private AutoCompleteTextView keywordEditText;
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


    private boolean locationPermissionGranted = false;
    private static final int LOCATION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationClient;
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private static final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(-85, 180) ,new LatLng(85, -180));


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // get references to views
        keywordEditText = view.findViewById(R.id.ma_sf_et_keyword); // TODO: 4/22/18 Google Autocomplete
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

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(view.getContext());

        autocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), mGeoDataClient, BOUNDS, null);
        keywordEditText.setAdapter(autocompleteAdapter);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        getLocationPermissions();

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

        try {
            Double.parseDouble(distance);
        } catch (Exception e) {
            distance = "10";
        }


        locationType = null;
        location = null;

        if (radioButton1.isChecked()) {
            locationType = "coords";
            if (location == null)
                location = "34.0266,-118.2831";
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
        } else {
            Toast.makeText(getActivity(), R.string.fix_errors, Toast.LENGTH_SHORT).show();
        }
    }

    public void getCurrentLocation() {

        try {
            if (locationPermissionGranted) {
                Log.d(TAG, "getCurrentLocation: Creating new task to get location");
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location locationResult) {
                                // Got last known location. In some rare situations this can be null.
                                Log.d(TAG, "onSuccess: successfully obtained location");

                                if (locationResult != null) {
                                    location = Double.toString(locationResult.getLatitude()) + "," + Double.toString(locationResult.getLongitude());
                                    Log.d(TAG, "onSuccess: location retrieved: " + location);
                                }
                            }
                        });
            }

        } catch (SecurityException e) {
            Toast.makeText(getActivity(), "Location permission denied. Cannot access device location.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocationPermissions() {

        // request permission if not granted
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermissions: permissions not granted. Trying to request for permissions");
            locationPermissionGranted = false;
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "getLocationPermissions: permissions granted. Trying to get location");
            locationPermissionGranted = true;
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        locationPermissionGranted = false;
                        return;
                    }
                }
                locationPermissionGranted = true;
                getCurrentLocation();
            }
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

        return validity;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
