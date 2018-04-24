package com.example.omijn.placeandentertainmentsearch;

public class PlaceResult {
    private String name;
    private String address;
    private String icon;
    private String place_id;
    private boolean isFavorite;

    public PlaceResult(String name, String address, String icon, String place_id, boolean isFavorite) {
        this.name = name;
        this.address = address;
        this.icon = icon;
        this.place_id = place_id;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favoriteState) {
        isFavorite = favoriteState;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getIcon() {
        return icon;
    }

    public String getPlace_id() {
        return place_id;
    }
}
