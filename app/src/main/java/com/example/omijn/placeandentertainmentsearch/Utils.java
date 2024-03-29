package com.example.omijn.placeandentertainmentsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

    static final class FavoriteCheckResult {
        private boolean isFavorite;
        private int favoritePosition;

        public FavoriteCheckResult(boolean isFavorite, int favoritePosition) {
            this.isFavorite = isFavorite;
            this.favoritePosition = favoritePosition;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public int getFavoritePosition() {
            return favoritePosition;
        }
    }

    public static FavoriteCheckResult isFavorite(PlaceResult placeResultToCheck, JSONArray favorites) {
        boolean isFavorite = false;
        int favoritePosition = -1;

        for (int i = 0; i < favorites.length(); i++) {
            JSONObject jObj;
            try {
                jObj = favorites.getJSONObject(i);
                if (jObj.getString("place_id").equals(placeResultToCheck.getPlace_id())) {
                    isFavorite = true;
                    favoritePosition = i;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new FavoriteCheckResult(isFavorite, favoritePosition);
    }

    public static FavoriteCheckResult isFavorite(String placeIdToCheck, JSONArray favorites) {
        boolean isFavorite = false;
        int favoritePosition = -1;

        for (int i = 0; i < favorites.length(); i++) {
            JSONObject jObj;
            try {
                jObj = favorites.getJSONObject(i);
                if (jObj.getString("place_id").equals(placeIdToCheck)) {
                    isFavorite = true;
                    favoritePosition = i;
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new FavoriteCheckResult(isFavorite, favoritePosition);
    }
}
