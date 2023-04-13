package com.example.yoursejahtera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.jetbrains.annotations.NotNull;

public class DisplayUserInfo extends AppCompatActivity {


        MaterialButton read;
        FirebaseFirestore db;

        static final String TAG = "Read Data Activity";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_user_info);
            db = FirebaseFirestore.getInstance();
            read = findViewById(R.id.readbtn);

            read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    db.collection("Registration")
                            //.whereEqualTo("Gender",switchstatus)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete( @NotNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(DisplayUserInfo.this,"Successful",Toast.LENGTH_LONG).show();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            String name = document.getString("name");
                                            TextView nameTextView = findViewById(R.id.nameTextView);
                                        nameTextView.setText("Name: " + name);
                                        }



                                    }else {

                                        Toast.makeText(DisplayUserInfo.this,"Failed",Toast.LENGTH_LONG).show();

                                    }

                                }
                            });

                }
            });
        }


//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_display_user_info);
//            db = FirebaseFirestore.getInstance();
//            read = findViewById(R.id.readbtn);
//            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//
//            read.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    db.collection("Registration")
//                            .document(userID)
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
//                                @Override
//                                public void onComplete( Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot document = task.getResult();
//                                        if (document.exists()) {
//                                            // Get the user data from the document
//                                            String name = document.getString("name");
//                                            //int age = document.getLong("age").intValue();
//                                            // etc.
////
////                                            TextView nameTextView = findViewById(R.id.nameTextView);
////
////
////                                            nameTextView.setText("Name: " + name);
//
//                                            Log.d(TAG, "Name: " + name + ", Age: " );
//                                        } else {
//                                            Log.d(TAG, "No such document");
//                                        }
//                                    } else {
//                                        Log.d(TAG, "get failed with ", task.getException());
//                                    }
//                                }
//                            });
//
//                }
//            });
//        }
}

