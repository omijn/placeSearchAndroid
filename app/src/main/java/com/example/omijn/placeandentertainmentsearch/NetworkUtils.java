package com.example.omijn.placeandentertainmentsearch;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String API_BASE_URL =  "http://active-avocado.us-east-2.elasticbeanstalk.com";

    public static URL buildUrl(String keyword, String category, String distanceMiles, String locationType, String location) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath("nearby")
                .appendQueryParameter("keyword", keyword)
                .appendQueryParameter("category", category)
                .appendQueryParameter("distance", distanceMiles)
                .appendQueryParameter("location_type", locationType)
                .appendQueryParameter("loc", location)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String buildDetailsUrl(String placeId) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath("details")
                .appendQueryParameter("place_id", placeId)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url.toString();
    }

}
