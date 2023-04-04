package com.example.yoursejahtera;

import org.osmdroid.util.GeoPoint;

public class Hospital {
    private String name;
    private GeoPoint location;

    public Hospital(String name, GeoPoint location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public GeoPoint getLocation() {
        return location;
    }
}

