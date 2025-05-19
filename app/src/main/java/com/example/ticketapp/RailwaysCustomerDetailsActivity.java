package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RailwaysCustomerDetailsActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextAge, editTextContact, editTextIDNumber;
    private Spinner spinnerGender;
    private Button buttonProceed;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_railways_customer_details);

        // ✅ Initialize UI elements
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextContact = findViewById(R.id.editTextContact);
        editTextIDNumber = findViewById(R.id.editTextIDNumber);
        spinnerGender = findViewById(R.id.spinnerGender);
        buttonProceed = findViewById(R.id.buttonProceed);

        // ✅ Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // ✅ Populate Spinner
        setupSpinner(spinnerGender, R.array.gender_options);

        // ✅ Button Click Event - Saves Data & Navigates to SearchTicketRailwaysActivity
        buttonProceed.setOnClickListener(v -> {
            Log.d("CustomerDetails", "Proceed Button Clicked!"); // ✅ Debugging
            if (validateInput()) {
                saveCustomerDetails();
            }
        });
    }

    // ✅ Method to set up a Spinner
    private void setupSpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayResource, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // ✅ Validate Input Fields
    private boolean validateInput() {
        String fullName = editTextFullName.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String idNumber = editTextIDNumber.getText().toString().trim();

        if (fullName.isEmpty() || !fullName.matches("[a-zA-Z ]+")) {
            editTextFullName.setError("Enter a valid full name (alphabets only)");
            return false;
        }

        if (ageStr.isEmpty()) {
            editTextAge.setError("Enter age");
            return false;
        }
        int age = Integer.parseInt(ageStr);
        if (age <= 0 || age > 120) {
            editTextAge.setError("Enter a valid age (1-120)");
            return false;
        }

        if (contact.isEmpty() || !contact.matches("\\d{10}")) {
            editTextContact.setError("Enter a valid 10-digit number");
            return false;
        }
        if (databaseHelper.isContactsExists(contact)) {
            editTextContact.setError("Contact number already exists!");
            return false;
        }

        if (idNumber.isEmpty() || !idNumber.matches("\\d{12}|[A-Z]{5}\\d{4}[A-Z]{1}|[A-Z0-9]{8,9}")) {
            editTextIDNumber.setError("Enter a valid Aadhar/PAN/Passport");
            return false;
        }
        if (databaseHelper.isIDNumberExists(idNumber)) {
            editTextIDNumber.setError("ID number already exists!");
            return false;
        }

        return true;
    }

    // ✅ Save Customer Details into Database
    private void saveCustomerDetails() {
        String fullName = editTextFullName.getText().toString().trim();
        int age = Integer.parseInt(editTextAge.getText().toString().trim());
        String gender = spinnerGender.getSelectedItem().toString();
        String contact = editTextContact.getText().toString().trim();
        String idNumber = editTextIDNumber.getText().toString().trim();

        Log.d("CustomerDetails", "Attempting to insert: " + fullName + ", Age: " + age + ", Gender: " + gender);

        boolean isInserted = databaseHelper.insertRailwaysCustomer(fullName, age, gender, contact, idNumber);

        if (isInserted) {
            Toast.makeText(this, "Details Saved Successfully!", Toast.LENGTH_SHORT).show();
            Log.d("CustomerDetails", "✅ Data Inserted! Navigating to SearchTicketRailwaysActivity...");

            new android.os.Handler().postDelayed(() -> {
                Intent intent = new Intent(RailwaysCustomerDetailsActivity.this, SearchTicketRailwaysActivity.class);
                startActivity(intent);
                finish();
            }, 500);
        } else {
            Toast.makeText(this, "Failed to Save Details!", Toast.LENGTH_SHORT).show();
            Log.e("CustomerDetails", "❌ Error saving customer details.");
        }
    }

}
