package com.example.yoursejahtera;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.Nullable;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import android.location.Geocoder;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity{

    private MapView map;
    private EditText searchEditText;

    private Button logoutBtn;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        logoutBtn = findViewById(R.id.logout_button);
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // Create a list of hospital coordinates in Malaysia
        List<Hospital> hospitals = new ArrayList<>();
        hospitals.add(new Hospital("Hopital Batu Gajah", new GeoPoint(4.47962161893849, 101.03502902934433))); // Hospital Batu Gajah
        hospitals.add(new Hospital("Hospital Changkat Melintang", new GeoPoint(4.314113336458469, 100.91074850790041)));
        hospitals.add(new Hospital("Hospital Gerik", new GeoPoint(5.429539250015331, 101.12800743701212)));
        hospitals.add(new Hospital("Hospital Kampar", new GeoPoint(4.313183021747455, 101.15649006130552)));
        hospitals.add(new Hospital("Hospital Kuala Kangsar", new GeoPoint(4.773835492339432, 100.93183935937937)));
        hospitals.add(new Hospital("Hospital Parit Buntar", new GeoPoint(5.132758637142821, 100.48291166496475)));
        hospitals.add(new Hospital("Hospital Raja Permaisuri Bainun", new GeoPoint(4.604603425956776, 101.09025143811998)));
        hospitals.add(new Hospital("Hospital Selama", new GeoPoint(5.213679174709079, 100.68883228920896)));
        hospitals.add(new Hospital("Hospital Seri Manjung", new GeoPoint(4.185865164684335, 100.66179258103662)));
        hospitals.add(new Hospital("Hospital Slim River", new GeoPoint(3.83835479008865, 101.40558441247406)));
        hospitals.add(new Hospital("Hospital Sungai Siput", new GeoPoint(4.82832545853154, 101.05701068638874)));
        hospitals.add(new Hospital("Hospital Taiping", new GeoPoint(4.8518421992829674, 100.73699095581402)));
        hospitals.add(new Hospital("Hospital Tapah", new GeoPoint(4.202709707369988, 101.25956879132842)));
        hospitals.add(new Hospital("Hospital Teluk Intan", new GeoPoint(4.005461020813196, 101.04053788719524)));

        // Loop through the list of hospital coordinates and add a marker for each one
        for (int i = 0; i < hospitals.size(); i++) {
            Hospital hospital = hospitals.get(i);
            String name = hospital.getName();
            GeoPoint location = hospital.getLocation();
            Marker marker = new Marker(map);
            marker.setPosition(location);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getResources().getDrawable(R.drawable.marker_icon));
            marker.setTitle(name);
            marker.setSnippet("Vaccine is available in this hospital.");
            marker.setInfoWindow(new CustomInfoWindow(map)); // Set the custom InfoWindow
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
//                FirebaseAuth.getInstance();
//                logoutBtn.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick (View v){
//                    FirebaseAuth.getInstance().signOut();
//                    Intent intent = new Intent(MapActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
//        });
            }
        });
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//
//            }
//        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent homeIntent = new Intent(MapActivity.this, MapActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.navigation_appointment:
                        Intent dashboardIntent = new Intent(MapActivity.this, Appointment.class);
                        startActivity(dashboardIntent);
                        return true;
                    case R.id.navigation_notifications:
                        Intent profileIntent = new Intent(MapActivity.this, Profile.class);
                        startActivity(profileIntent);
                        return true;
                    default:
                        return false;
                }
//                default:
//                return false;
            }
        });
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // Logout was successful
//                            // You can do any necessary actions here, like redirecting to the login screen
//                            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        } else {
//                            // Logout failed
//                            // Handle the error appropriately, e.g. by displaying a toast or logging an error message
//                        }
//                    }
//                });


//        Button logoutButton = findViewById(R.id.logout_button);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Sign out from Firebase Auth
//                mAuth.signOut();
//
//                // Sign out from Google Sign In client
//                mGoogleSignInClient.signOut()
//                        .addOnCompleteListener(MapActivity.this, new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete( Task<Void> task) {
//                                // Redirect to login activity
//                                Intent intent = new Intent(MapActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//            }
//        });







    }

//FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    public void signOut() {
//        // [START auth_sign_out]
//       FirebaseAuth.getInstance().signOut();
//
//    }

}
