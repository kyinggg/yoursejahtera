package com.example.yoursejahtera;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Appointment extends AppCompatActivity {

    private TextView hospitalNameTextView;
    private Button chooseTimeButton;
    private Button chooseDateButton;
    private Button submitButton;

    private int selectedHour;
    private int selectedMinute;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        hospitalNameTextView = findViewById(R.id.hospital_name_text_view);
        chooseTimeButton = findViewById(R.id.choose_time_button);
        chooseDateButton = findViewById(R.id.choose_date_button);
        submitButton = findViewById(R.id.submit_button);

        // Get the hospital name from the intent extra
        String hospitalName = getIntent().getStringExtra("name");
        hospitalNameTextView.setText(hospitalName);




        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Appointment.this,
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
                                    chooseTimeButton.setText(selectedHour + ":0" + selectedMinute);
                                }else{
                                    chooseTimeButton.setText(selectedHour + ":" + selectedMinute);
                                }

                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }

        });

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Appointment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectedYear = year;
                                selectedMonth = month;
                                selectedDay = dayOfMonth;


                                chooseDateButton.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });




        submitButton.setOnClickListener(new View.OnClickListener() {
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
                int month = selectedMonth + 1;
                int day = selectedDay;
                String dateString = String.format("%d-%02d-%02d", year, month, day);

                //Toast.makeText(Appointment.this, "Hello your appointment is on " + formattedTime + dateString, Toast.LENGTH_SHORT).show();
                Map<String, Object> appointment = new HashMap<>();
                String TIME = formattedTime;
                String DATE = dateString;
                appointment.put("time:", TIME);
                appointment.put("date:", DATE);
                appointment.put("hospital", hospitalName);

                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                db.collection("users").document(userUid).collection("appointments").document().set(appointment)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Appointment.this, "Data saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(Appointment.this, "Data not saved", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });




    }
}

