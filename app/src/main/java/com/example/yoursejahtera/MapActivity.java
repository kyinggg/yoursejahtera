package com.example.yoursejahtera;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import android.location.Geocoder;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity {

    private MapView map;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_map2);
        map = findViewById(R.id.map_view);
        searchEditText = findViewById(R.id.search_location);
        Button searchButton = findViewById(R.id.search_button);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.getController().setZoom(5);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2945);
        map.getController().setCenter(startPoint);

        // Create a list of hospital coordinates in Malaysia
        List<GeoPoint> hospitalCoords = new ArrayList<>();
        hospitalCoords.add(new GeoPoint(4.47962161893849, 101.03502902934433)); // Hospital Batu Gajah
        hospitalCoords.add(new GeoPoint(4.314113336458469, 100.91074850790041)); // Hospital Changkat Melintang
        hospitalCoords.add(new GeoPoint(5.429539250015331, 101.12800743701212)); // Hospital Gerik
        hospitalCoords.add(new GeoPoint(4.313183021747455, 101.15649006130552)); // Hospital Kampar
        hospitalCoords.add(new GeoPoint(4.773835492339432, 100.93183935937937)); // Hospital Kuala Kangsar
        hospitalCoords.add(new GeoPoint(5.132758637142821, 100.48291166496475)); // Hospital Parit Buntar
        hospitalCoords.add(new GeoPoint(4.604603425956776, 101.09025143811998)); // Hospital Raja Permaisuri Bainun
        hospitalCoords.add(new GeoPoint(5.213679174709079, 100.68883228920896)); // Hospital Selama
        hospitalCoords.add(new GeoPoint(4.185865164684335, 100.66179258103662)); // Hospital Seri Manjung
        hospitalCoords.add(new GeoPoint(3.83835479008865, 101.40558441247406)); // Hospital Slim River
        hospitalCoords.add(new GeoPoint(4.82832545853154, 101.05701068638874)); // Hospital Sungai Siput,
        hospitalCoords.add(new GeoPoint(4.8518421992829674, 100.73699095581402)); // Hospital Taiping
        hospitalCoords.add(new GeoPoint(4.202709707369988, 101.25956879132842)); // Hospital Tapah
        hospitalCoords.add(new GeoPoint(4.005461020813196, 101.04053788719524)); // Hospital Teluk Intan

        // Loop through the list of hospital coordinates and add a marker for each one
        for (int i = 0; i < hospitalCoords.size(); i++) {
            GeoPoint coord = hospitalCoords.get(i);
            Marker marker = new Marker(map);
            marker.setPosition(coord);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getResources().getDrawable(R.drawable.marker_icon));
            marker.setTitle("Hospital " + (i+1));
            marker.setSnippet("This is the information for Hospital " + (i+1));
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    Toast.makeText(getApplicationContext(), marker.getSnippet(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            map.getOverlays().add(marker);
        }


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEditText.getText().toString();
                if (searchString.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a location", Toast.LENGTH_SHORT).show();
                    return;
                }

                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocationName(searchString, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        android.location.Address address = addresses.get(0);
                        double latitude = address.getLatitude();
                        double longitude = address.getLongitude();
                        GeoPoint searchLocation = new GeoPoint(latitude, longitude);
                        map.getController().setCenter(searchLocation);
                        map.getController().setZoom(15);

                    } else {
                        Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}