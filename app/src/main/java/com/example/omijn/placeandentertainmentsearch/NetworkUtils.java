package com.example.omijn.placeandentertainmentsearch;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String API_BASE_URL =  "http://hw8-victorykashyap601206.codeanyapp.com:8081/nearby";

    public static URL buildUrl(String placeName) {
        Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendQueryParameter("keyword", placeName)
                .appendQueryParameter("category", "default")
                .appendQueryParameter("distance", "10")
                .appendQueryParameter("location_type", "coords")
                .appendQueryParameter("loc", "34.0266,-118.2831")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
