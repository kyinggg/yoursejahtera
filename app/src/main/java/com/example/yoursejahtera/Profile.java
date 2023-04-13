package com.example.yoursejahtera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class Profile extends AppCompatActivity {
    private Button logoutBtn;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutBtn = findViewById(R.id.logout_button);
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

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public void signOut() {
        FirebaseAuth.getInstance().signOut();

    }


}