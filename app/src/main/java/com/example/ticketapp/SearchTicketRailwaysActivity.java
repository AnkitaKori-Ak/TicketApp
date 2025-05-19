package com.example.ticketapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class SearchTicketRailwaysActivity extends AppCompatActivity {
    private Spinner spinnerDeparture, spinnerArrival, spinnerClassPreference;
    private EditText editTextDate;
    private Button buttonSearch;
    private String selectedDeparture, selectedArrival, selectedClass, travelDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ticket_railways);

        // Initialize UI components
        spinnerDeparture = findViewById(R.id.spinnerDeparture);
        spinnerArrival = findViewById(R.id.spinnerArrival);
        spinnerClassPreference = findViewById(R.id.spinnerClassPreference);
        editTextDate = findViewById(R.id.editTextDate);
        buttonSearch = findViewById(R.id.buttonSearch);

        // Populate Spinners
        setupSpinners();

        // Date Picker for travel date selection
        editTextDate.setOnClickListener(v -> showDatePicker());

        // Search Button Click - Pass data to SelectTicketsRailwaysActivity
        buttonSearch.setOnClickListener(v -> {
            travelDate = editTextDate.getText().toString().trim();
            if (validateInputs()) {
                Intent intent = new Intent(SearchTicketRailwaysActivity.this, SelectTicketsRailwaysActivity.class);
                intent.putExtra("departure", selectedDeparture);
                intent.putExtra("arrival", selectedArrival);
                intent.putExtra("travelDate", travelDate);
                intent.putExtra("classPreference", selectedClass);
                startActivity(intent);
            }
        });
    }

    private void setupSpinners() {
        setupSpinner(spinnerDeparture, R.array.train_stations);
        setupSpinner(spinnerArrival, R.array.train_stations);
        setupSpinner(spinnerClassPreference, R.array.train_classes);
    }

    private void setupSpinner(Spinner spinner, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner == spinnerDeparture) selectedDeparture = parent.getItemAtPosition(position).toString();
                if (spinner == spinnerArrival) selectedArrival = parent.getItemAtPosition(position).toString();
                if (spinner == spinnerClassPreference) selectedClass = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Validate input fields
    private boolean validateInputs() {
        if (selectedDeparture.equals(selectedArrival)) {
            Toast.makeText(this, "Departure and Arrival cannot be the same!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a travel date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
