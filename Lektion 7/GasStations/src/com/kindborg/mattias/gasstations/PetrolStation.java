package com.kindborg.mattias.gasstations;

import com.google.android.gms.maps.model.*;

public class PetrolStation {

    private final LatLng latlng;
    private final String city;
    private final String name;

    public PetrolStation(double longitude, double latitude, String city, String name) {
        this.latlng = new LatLng(latitude, longitude);
        this.city = city;
        this.name = name;
    }

    public LatLng getLatLng() {
        return latlng;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }
}
