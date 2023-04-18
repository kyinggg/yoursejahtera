package com.example.yoursejahtera;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



public class ChangeAppointment extends AppCompatActivity {

    private TextView hospitalNameTextView;
    private Button changeTypeButton;
    private Button changeTypeButtonTime;
    private Button changeDateButton;
    private Button changeButton;
    private Button changeTimeButton;
    private int selectedHour;
    private int selectedMinute;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private String selectedVaccineType;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BottomNavigationView bottomNavigationView;
    private String userId;
    private String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_appointment);

        hospitalNameTextView = findViewById(R.id.hospital_name_text_view);
        changeTypeButton = findViewById(R.id.change_type_button);
        changeTimeButton = findViewById(R.id.change_time_button);
        changeDateButton = findViewById(R.id.change_date_button);
        changeButton = findViewById(R.id.change_button);
        String hospitalName = getIntent().getStringExtra("hospitalName");

//        hospitalNameTextView.setText(hospitalName);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        changeTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an array of strings containing the names of the 3 vaccine types
                String[] vaccineTypes = {"First Dose", "Second Dose", "Booster"};

                // Create an AlertDialog to display the vaccine types as a list
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAppointment.this);
                builder.setTitle("Choose vaccine type");
                builder.setItems(vaccineTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the selected vaccine type
                        selectedVaccineType = vaccineTypes[which];
                        changeTypeButton.setText(selectedVaccineType);
                    }
                });
                builder.show();
            }
        });
        changeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ChangeAppointment.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Limit the available hours to 9am to 5pm
                                if (hourOfDay < 9) {
                                    Toast.makeText(getApplicationContext(), "Only choose from 9am to 5pm", Toast.LENGTH_SHORT).show();
                                } else if (hourOfDay >= 17) {
                                    Toast.makeText(getApplicationContext(), "Only choose from 9am to 5pm", Toast.LENGTH_SHORT).show();
                                } else {
                                    selectedHour = hourOfDay;
                                    selectedMinute = minute;
                                }

                                if(selectedMinute<10){
                                    changeTimeButton.setText(selectedHour + ":0" + selectedMinute);
                                }else{
                                    changeTimeButton.setText(selectedHour + ":" + selectedMinute);
                                }

                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }

        });
        changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ChangeAppointment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectedYear = year;
                                selectedMonth = month;
                                selectedDay = dayOfMonth;
                                changeDateButton.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int hour = selectedHour;
                int minutes = selectedMinute;
                // Create LocalTime object
                LocalTime time = LocalTime.of(hour, minutes);
                // Format the time
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = time.format(formatter);

                int year = selectedYear;
                int month = selectedMonth + 1; // Add 1 because Calendar.MONTH is zero-based
                int day = selectedDay;
                String dateString = String.format("%d-%02d-%02d", year, month, day);




                db.collection("users").document(userUid).collection("appointments")


                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Retrieve the appointment ID from the first document in the query result
                                String appointmentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                                // Create a Map to hold the updated data
                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("vaccine type", selectedVaccineType);
                                updatedData.put("date:", dateString);
                                updatedData.put("time:", formattedTime);
                                updatedData.put("hospital", hospitalName);

                                // Update the document in Firestore
                                DocumentReference appointmentRef = db.collection("users").document(userUid)
                                        .collection("appointments").document(appointmentId);
                                appointmentRef.set(updatedData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Appointment data updated successfully
                                            Toast.makeText(ChangeAppointment.this, "Appointment updated successfully!" + hospitalName, Toast.LENGTH_SHORT).show();

                                            // Go back to the previous activity
                                            Intent intent = new Intent(ChangeAppointment.this, DisplayUserInfo.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            // Failed to update appointment data
                                            Toast.makeText(ChangeAppointment.this, "Failed to update appointment data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            } else {
                                // Appointment not found
                                Toast.makeText(ChangeAppointment.this, "Appointment not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Error occurred while retrieving appointment data
                            Toast.makeText(ChangeAppointment.this, "Error occurred while retrieving appointment data", Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });
    }
}
