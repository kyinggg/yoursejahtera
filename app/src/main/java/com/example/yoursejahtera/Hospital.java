package com.example.yoursejahtera;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.osmdroid.util.GeoPoint;

public class Hospital {
    private String name;
    private GeoPoint location;
    private BottomNavigationView bottomNavigationView;
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

