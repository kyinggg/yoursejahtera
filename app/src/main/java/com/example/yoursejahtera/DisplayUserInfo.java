package com.example.yoursejahtera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DisplayUserInfo extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Button appointmentBtn;
    MaterialButton read;
    FirebaseFirestore db;
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    static final String TAG = "Read Data Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_info);
        db = FirebaseFirestore.getInstance();
        String hospitalName = getIntent().getStringExtra("hospitalName");

       // String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // read.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {



        db.collection("users").document(userID).collection("registration")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get the registration data from the document
                                String name = document.getString("name");


                                TextView nameTextView = findViewById(R.id.nameTextView);
                                nameTextView.setText("Name: " + name);

                                String ic = document.getString("ic");
                                TextView icTextView = findViewById(R.id.icTextView);
                                icTextView.setText("IC Number: " + ic);

                                String gender = document.getString("gender");
                                TextView genderTextView = findViewById(R.id.genderTextView);
                                genderTextView.setText("Gender: " + gender);

                                String phone = document.getString("phone");
                                TextView phoneTextView = findViewById(R.id.phoneTextView);
                                phoneTextView.setText("Phone Number: " + phone);



                            }
                            // Retrieve Appointment data
                            db.collection("users").document(userID).collection("appointments")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                   @Override
                                   public void onComplete( Task<QuerySnapshot> task) {
                                       if (task.isSuccessful()) {
                                           for (QueryDocumentSnapshot document : task.getResult()) {
                                               // Get the appointment data from the document
                                               String hospitalName = document.getString("hospital_name");



                                               String date = document.getString("date:");
                                               TextView dateTextView = findViewById(R.id.dateTextView);
                                               dateTextView.setText("Appointment Date: " + date);

                                               String hospital = document.getString("hospital");
                                               TextView hospitalTextView = findViewById(R.id.hospitalTextView);
                                               hospitalTextView.setText("Hospital Name: " + hospital);

                                               String time = document.getString("time:");
                                               TextView timeTextView = findViewById(R.id.timeTextView);
                                               timeTextView.setText("Appointment Time: " + time);

                                               String vaccine_type = document.getString("vaccine type");
                                               TextView vaccineTextView = findViewById(R.id.vaccineTextView);
                                               vaccineTextView.setText("Vaccine Type: " + vaccine_type);
                                           }
                                       }
                                       else {
                                           Log.d(TAG, "Error getting appointment documents: ", task.getException());
                                       }
                                   }
                               });
                        } else {
                            Log.d(TAG, "Error getting registration documents: ", task.getException());
                        }
                    }

                });
        Button appointmentBtn = findViewById(R.id.appointmentBtn);
        appointmentBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // Create a new intent to launch the target activity
              Intent changeIntent = new Intent(DisplayUserInfo.this, ChangeAppointment.class);
              changeIntent.putExtra("hospitalName", hospitalName);

              // Start the target activity
              startActivity(changeIntent);
          }
      });



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent homeIntent = new Intent(DisplayUserInfo.this, DisplayUserInfo.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.navigation_appointment:
                        Intent dashboardIntent = new Intent(DisplayUserInfo.this, MapActivity.class);
                        startActivity(dashboardIntent);
                        return true;
                    case R.id.navigation_notifications:
                        Intent profileIntent = new Intent(DisplayUserInfo.this, Profile.class);
                        startActivity(profileIntent);
                        return true;
                    default:
                        return false;
                }

            }
        });
    }


}


