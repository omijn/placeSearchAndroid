package com.example.omijn.placeandentertainmentsearch;

public class PlaceResult {
    private String name;
    private String address;

    public PlaceResult(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
