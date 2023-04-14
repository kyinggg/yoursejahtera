package com.example.yoursejahtera;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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
    private Button chooseTypeButton;
    private Button chooseTimeButton;
    private Button chooseDateButton;
    private Button submitButton;

    private int selectedHour;
    private int selectedMinute;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private String selectedVaccineType;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        hospitalNameTextView = findViewById(R.id.hospital_name_text_view);
        chooseTypeButton = findViewById(R.id.choose_type_button);
        chooseTimeButton = findViewById(R.id.choose_time_button);
        chooseDateButton = findViewById(R.id.choose_date_button);
        submitButton = findViewById(R.id.submit_button);

        // Get the hospital name from the intent extra
        String hospitalName = getIntent().getStringExtra("name");
        hospitalNameTextView.setText(hospitalName);

        chooseTypeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create an array of strings containing the names of the 3 vaccine types
                String[] vaccineTypes = {"First Dose", "Second Dose", "Booster"};

                // Create an AlertDialog to display the vaccine types as a list
                AlertDialog.Builder builder = new AlertDialog.Builder(Appointment.this);
                builder.setTitle("Choose vaccine type");
                builder.setItems(vaccineTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the selected vaccine type
                        selectedVaccineType = vaccineTypes[which];
                        chooseTypeButton.setText(selectedVaccineType);
                    }
                });
                builder.show();
            }
        });


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
                String VACCINE_TYPE = selectedVaccineType;
                appointment.put("time:", TIME);
                appointment.put("date:", DATE);
                appointment.put("hospital", hospitalName);
                appointment.put("vaccine type",selectedVaccineType);

                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                db.collection("users").document(userUid).collection("appointments").document().set(appointment)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Appointment.this, "Data saved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Appointment.this,  DisplayUserInfo.class);
                                startActivity(i);
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

