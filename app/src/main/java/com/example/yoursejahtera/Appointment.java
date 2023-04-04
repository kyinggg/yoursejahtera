package com.example.yoursejahtera;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Appointment extends AppCompatActivity {

    private TextView hospitalNameTextView;
    private Button chooseTimeButton;
    private Button chooseDateButton;

    private int selectedHour;
    private int selectedMinute;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        hospitalNameTextView = findViewById(R.id.hospital_name_text_view);
        chooseTimeButton = findViewById(R.id.choose_time_button);
        chooseDateButton = findViewById(R.id.choose_date_button);

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
    }
}
