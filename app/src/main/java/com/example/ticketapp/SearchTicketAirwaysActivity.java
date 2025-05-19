package com.example.ticketapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class SearchTicketAirwaysActivity extends AppCompatActivity {

    private Spinner spinnerDeparture, spinnerArrival, spinnerClassPreference;
    private EditText editTextDate;
    private Switch switchTripType;
    private Button buttonSearch;
    private String selectedDeparture, selectedArrival, selectedClass, tripType = "One Way"; // Default to One Way

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ticket_airways);

        // Initialize UI elements
        spinnerDeparture = findViewById(R.id.spinnerDeparture);
        spinnerArrival = findViewById(R.id.spinnerArrival);
        spinnerClassPreference = findViewById(R.id.spinnerClassPreference);
        editTextDate = findViewById(R.id.editTextDate);
        switchTripType = findViewById(R.id.switchTripType);
        buttonSearch = findViewById(R.id.buttonSearch);

        // Populate Spinners
        setupSpinners();

        // Date Picker for Travel Date (Fixed)
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Handle Switch (One Way / Round Trip)
        switchTripType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tripType = isChecked ? "Round Trip" : "One Way";
        });

        // Search Button Click
        buttonSearch.setOnClickListener(v -> searchTickets());
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> flightAdapter = ArrayAdapter.createFromResource(this,
                R.array.flight_locations, android.R.layout.simple_spinner_item);
        flightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeparture.setAdapter(flightAdapter);
        spinnerArrival.setAdapter(flightAdapter);

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this,
                R.array.class_preferences, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassPreference.setAdapter(classAdapter);

        spinnerClassPreference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // ✅ FIXED: Show Date Picker
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Correct month value (Months in Calendar start from 0)
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editTextDate.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void searchTickets() {
        String travelDate = editTextDate.getText().toString().trim();

        if (travelDate.isEmpty()) {
            Toast.makeText(this, "Please select a travel date!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Searching " + tripType + " flights in " + selectedClass +
                " on " + travelDate, Toast.LENGTH_LONG).show();

        String searchFlightDetails = "From: " + spinnerDeparture.getSelectedItem().toString() +
                " To: " + spinnerArrival.getSelectedItem().toString() +
                " Date: " + travelDate + " Class: " + selectedClass;

        Intent intent = new Intent(SearchTicketAirwaysActivity.this, SelectTicketsActivity.class);
        intent.putExtra("searchedFlightDetails", searchFlightDetails); // ✅ Pass search details
        startActivity(intent);

    }
}
