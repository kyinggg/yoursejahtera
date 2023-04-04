package com.example.yoursejahtera;

import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class CustomInfoWindow extends MarkerInfoWindow {

    public CustomInfoWindow(MapView mapView){
        super(R.layout.custom_info_window, mapView);
    }

    @Override
    public void onOpen(Object item){
        Marker marker = (Marker) item;
        TextView titleTextView = (TextView) mView.findViewById(R.id.info_window_title);
        TextView snippetTextView = (TextView) mView.findViewById(R.id.info_window_snippet);
        titleTextView.setText(marker.getTitle());
        snippetTextView.setText(marker.getSnippet());
    }
}
