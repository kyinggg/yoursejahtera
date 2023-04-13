package com.example.yoursejahtera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private TextView name;
    private TextView ic;
    private TextView address;
    private TextView phone;
    private Spinner gender;
    private Button register;

    private String Name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//
//        //spinner for gender selection
//        Spinner genderSpinner = findViewById(R.id.gender_spinner);
//        String[] genderOptions = new String[]{"Male", "Female", "Other"};
//        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderOptions);
//        genderSpinner.setAdapter(genderAdapter);
//
//        name = findViewById(R.id.name_edit_text);
//        ic = findViewById(R.id.ic_edit_text);
//        address = findViewById(R.id.address_edit_text);
//        phone = findViewById(R.id.phone_edit_text);
//        gender = findViewById(R.id.gender_spinner);
//        register = findViewById(R.id.registerBtn);
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String Name = name.getText().toString();
//                String IC = ic.getText().toString();
//                String Address = address.getText().toString();
//                String Phone_Number = phone.getText().toString();
//                String selectedGender = gender.getSelectedItem().toString();
//
//                if (selectedGender.equals("Select Gender")) {
//                    Toast.makeText(Registration.this, "Please select your gender", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//                validateUser(Name, IC, Address, Phone_Number);
//                Toast.makeText(Registration.this, Name +","+ IC+","+Address+","+Phone_Number+","+selectedGender, Toast.LENGTH_SHORT).show();
//
//
//                Map<String, Object> registration = new HashMap<>();
//                String NAME = Name;
//                String Ic = IC;
//                String ADDRESS = Address;
//                String PHONE = Phone_Number;
//                String GENDER = selectedGender;
//
//                registration.put("name:", NAME);
//                registration.put("ic:", Ic);
//                registration.put("address:", ADDRESS);
//                registration.put("phone:", PHONE);
//                registration.put("gender",GENDER);
//
////                db.collection("Registration").add(registration)
////                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
////                            @Override
////                            public void onSuccess(DocumentReference documentReference) {
////                                Toast.makeText(Registration.this, "Data saved", Toast.LENGTH_SHORT).show();
////                            }
////                        })
////                        .addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            public void onFailure(Exception e) {
////                                Toast.makeText(Registration.this, "Data not saved", Toast.LENGTH_SHORT).show();
////                            }
////                        });
//                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                db.collection("users").document(userUid).collection("appointments").document().set(registration)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(Registration.this, "Data saved", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(Exception e) {
//                                Toast.makeText(Registration.this, "Data not saved", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                Intent i = new Intent(Registration.this, LoginActivity.class);
//                startActivity(i);
//            }
//        });
//
//    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //spinner for gender selection
        Spinner genderSpinner = findViewById(R.id.gender_spinner);
        String[] genderOptions = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderOptions);
        genderSpinner.setAdapter(genderAdapter);

        name = findViewById(R.id.name_edit_text);
        ic = findViewById(R.id.ic_edit_text);
        address = findViewById(R.id.address_edit_text);
        phone = findViewById(R.id.phone_edit_text);
        gender = findViewById(R.id.gender_spinner);
        register = findViewById(R.id.registerBtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String IC = ic.getText().toString();
                String Address = address.getText().toString();
                String Phone_Number = phone.getText().toString();
                String selectedGender = gender.getSelectedItem().toString();

                if (selectedGender.equals("Select Gender")) {
                    Toast.makeText(Registration.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate user input
                if (!validateUser(Name, IC, Address, Phone_Number)) {
                    return;
                }

                Map<String, Object> registration = new HashMap<>();
                registration.put("name", Name);
                registration.put("ic", IC);
                registration.put("address", Address);
                registration.put("phone", Phone_Number);
                registration.put("gender", selectedGender);

                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                db.collection("users")
                        .document(userUid)
                        .collection("registration").document().set(registration)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess( Void aVoid) {

                                Toast.makeText(Registration.this, "Data saved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Registration.this, LoginActivity.class);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Log.w("Registration", "Error adding document", e);
                                Toast.makeText(Registration.this, "Data not saved", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    //validate
    private Boolean validateUser(String Name, String IC, String Address, String Phone_Number) {
        //validate first name
        if (Name.length() == 0) {
            name.requestFocus();
            name.setError("Field Cannot be Empty");
            return false;
        } else if (!Name.matches("[a-zA-Z]+")) {
            name.requestFocus();
            name.setError("Enter Only Alphabetical Characters");
            return false;
        }

        //validate last name
        else if (IC.length() == 0) {
            ic.requestFocus();
            ic.setError("Field Cannot be Empty");
            return false;
        } else if (!IC.matches("^\\d{6}-\\d{2}-\\d{4}$")) {
            ic.requestFocus();
            ic.setError("Enter Numbers with Dash");
            return false;
        }

        //validate user name
        else if (!Address.matches("[a-zA-Z0-9\\s,.-/]+")) {
            address.requestFocus();
            address.setError("Enter only alphanumeric characters and special characters like spaces, commas, and periods");
            return false;
        }

        //validate contact
        else if (Phone_Number.length() > 10) {
            phone.requestFocus();
            phone.setError("Exceeded the limitation");
            return false;
        } else if (!Phone_Number.matches("^[0-9]{9,12}$")) {
            phone.requestFocus();
            phone.setError("Correct Format: 01xxxxxxxx");
            return false;
        } else {
            // Format the phone number as "+60xxxxxxxxx"
            if (Phone_Number.charAt(0) == '0') {
                Phone_Number = "+60" + Phone_Number.substring(1);
            }
            return true;
        }
    }

}