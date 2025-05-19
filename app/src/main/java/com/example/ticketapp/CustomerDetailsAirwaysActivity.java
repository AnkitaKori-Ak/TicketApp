package com.example.ticketapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class CustomerDetailsAirwaysActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextPassport, editTextDOB, editTextContact;
    private Button buttonSubmit;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_airways);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPassport = findViewById(R.id.editTextPassport);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextContact = findViewById(R.id.editTextContact);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        dbHelper = new DatabaseHelper(this);

        // Date Picker
        editTextDOB.setOnClickListener(v -> showDatePicker());

        buttonSubmit.setOnClickListener(v -> saveCustomerDetails());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) ->
                editTextDOB.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear), year, month, day);
        datePickerDialog.show();
    }


private void saveCustomerDetails() {
    String fullName = editTextFullName.getText().toString().trim();
    String passport = editTextPassport.getText().toString().trim();
    String dob = editTextDOB.getText().toString().trim();
    String contact = editTextContact.getText().toString().trim();

    if (!validateInputs(fullName, passport, dob, contact)) return;

    // Check for duplicate passport and contact
    if (dbHelper.isPassportExists(passport)) {
        editTextPassport.setError("Passport number already registered!");
        return;
    }
    if (dbHelper.isContactExists(contact)) {
        editTextContact.setError("Contact number already registered!");
        return;
    }

    // Insert data into the database
    boolean isInserted = dbHelper.insertAirwaysCustomer(fullName, passport, dob, contact);

    if (isInserted) {
        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();

        // ✅ Navigate to SearchTicketAirwaysActivity
        Intent intent = new Intent(CustomerDetailsAirwaysActivity.this, SearchTicketAirwaysActivity.class);
        startActivity(intent);

        // ✅ Close the current activity
        finish();
    } else {
        Toast.makeText(this, "Error saving details!", Toast.LENGTH_SHORT).show();
    }
}



    private boolean validateInputs(String fullName, String passport, String dob, String contact) {
        if (fullName.isEmpty()) {
            editTextFullName.setError("Enter full name");
            return false;
        }
        if (!passport.matches("[A-Z0-9]{6,9}")) {
            editTextPassport.setError("Enter a valid passport number");
            return false;
        }
        if (dob.isEmpty()) {
            editTextDOB.setError("Select date of birth");
            return false;
        }
        if (!contact.matches("\\d{10}")) {
            editTextContact.setError("Enter a valid 10-digit phone number");
            return false;
        }
        return true;
    }

}
