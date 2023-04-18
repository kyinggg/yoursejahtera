package com.example.yoursejahtera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    private Button logoutBtn;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private TextView textViewWelcome, textViewFullName, textViewIc, textViewAddress, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private String fullName, ic, address, gender, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        getSupportActionBar().setTitle("Profile");
//        textViewWelcome = findViewById(R.id.textView_show_welcome);
//        textViewFullName = findViewById(R.id.textView_show_full_name);
//        textViewIc = findViewById(R.id.textView_show_ic);
//        textViewAddress = findViewById(R.id.textView_show_address);
//        textViewGender = findViewById(R.id.textView_show_gender);
//        textViewMobile = findViewById(R.id.textView_show_mobile);
//        progressBar = findViewById(R.id.progressBar);

        logoutBtn = findViewById(R.id.logout_button);

//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if(firebaseUser == null){
//            Toast.makeText(Profile.this, "Something went wrong! User' s details are not available at the moment.", Toast.LENGTH_SHORT).show();
//        }else{
//            progressBar.setVisibility((View.VISIBLE));
//            //showUserProfile(firebaseUser);
//        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();

            }
        });



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent homeIntent = new Intent(Profile.this, DisplayUserInfo.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.navigation_appointment:
                        Intent dashboardIntent = new Intent(Profile.this, MapActivity.class);
                        startActivity(dashboardIntent);
                        return true;
                    case R.id.navigation_notifications:
                        Intent profileIntent = new Intent(Profile.this, Profile.class);
                        startActivity(profileIntent);
                        return true;
                }
                return false;
            }
        });

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase Auth
                mAuth.signOut();

                // Sign out from Google Sign In client
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(Profile.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete( Task<Void> task) {
                                // Redirect to login activity
                                Intent intent = new Intent(Profile.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

    }

//    private void showUserProfile(FirebaseUser firebaseUser) {
//        String userID = firebaseUser.getUid();
//
//        //extracting user reference from database for "registered users"
//        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("registrations");
//        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
//                if( readUserDetails != null){
//                    fullName = firebaseUser.getDisplayName();
//                    ic = readUserDetails.ic;
//                    address = readUserDetails.address;
//                    gender = readUserDetails.gender;
//                    mobile = readUserDetails.moblie;
//
//                    textViewWelcome.setText("Welcome," + fullName + "!");
//                    textViewFullName.setText(fullName);
//                    textViewIc.setText(ic);
//                    textViewAddress.setText(address);
//                    textViewGender.setText(gender);
//                    textViewMobile.setText(mobile);
//
//                }
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(Profile.this,"something went wrong!",Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
//    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public void signOut() {
        FirebaseAuth.getInstance().signOut();

    }


}
