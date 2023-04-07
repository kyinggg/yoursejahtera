package com.example.yoursejahtera;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class CustomInfoWindow extends MarkerInfoWindow {
    private BottomNavigationView bottomNavigationView;
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

        Button appointmentButton = (Button) mView.findViewById(R.id.info_window_button);
        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), Appointment.class);
                in.putExtra("name",marker.getTitle());
                v.getContext().startActivity(in);
            }
        });


    }

}
