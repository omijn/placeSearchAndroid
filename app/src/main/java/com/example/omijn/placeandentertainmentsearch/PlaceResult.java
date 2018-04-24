package com.example.omijn.placeandentertainmentsearch;

public class PlaceResult {
    private String name;
    private String address;
    private String icon;
    private String place_id;
    private String coordinates;
    private boolean isFavorite;

    public PlaceResult(String name, String address, String icon, String place_id, String coordinates) {
        this.name = name;
        this.address = address;
        this.icon = icon;
        this.place_id = place_id;
        this.coordinates = coordinates;
        this.isFavorite = false;
    }

    public void setFavorite(boolean favoriteState) {
        isFavorite = favoriteState;
    }

    public boolean isFavorite() {
        return isFavorite;
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

    public String getCoordinates() {
        return coordinates;
    }
}
