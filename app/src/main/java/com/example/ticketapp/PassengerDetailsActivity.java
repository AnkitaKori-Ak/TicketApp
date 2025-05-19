package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PassengerDetailsActivity extends AppCompatActivity {

    private EditText editTextAdults, editTextChildren, editTextEmail, editTextPhone;
    private Button buttonProceed;
    private LinearLayout passengerContainer;
    private List<EditText> passengerNames, passengerAges;
    private List<RadioGroup> passengerGenders;
    private DatabaseHelper dbHelper;
    private int selectedFlightPrice;
    private String searchedFlightDetails, selectedMeal;
    private FlightModel selectedFlight;
    private ArrayList<String> selectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);

        editTextAdults = findViewById(R.id.editTextAdults);
        editTextChildren = findViewById(R.id.editTextChildren);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonProceed = findViewById(R.id.buttonProceed);
        passengerContainer = findViewById(R.id.passengerContainer);

        dbHelper = new DatabaseHelper(this);
        passengerNames = new ArrayList<>();
        passengerAges = new ArrayList<>();
        passengerGenders = new ArrayList<>();

        selectedFlightPrice = getIntent().getIntExtra("FlightPrice", 0);
        searchedFlightDetails = getIntent().getStringExtra("searchedFlightDetails");
        selectedFlight = getIntent().getParcelableExtra("selectedFlight");
        selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");
        selectedMeal = getIntent().getStringExtra("selectedMeal");

        if (searchedFlightDetails == null || selectedFlight == null || selectedSeats == null || selectedSeats.isEmpty() || selectedMeal == null) {
            Log.e("PassengerDetailsActivity", "Error: Missing flight details!");
            Toast.makeText(this, "Error: Missing flight details!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextAdults.setOnFocusChangeListener((v, hasFocus) -> generatePassengerFields());
        editTextChildren.setOnFocusChangeListener((v, hasFocus) -> generatePassengerFields());

        buttonProceed.setOnClickListener(v -> savePassengerDetails());
    }

    private void generatePassengerFields() {
        passengerContainer.removeAllViews();
        passengerNames.clear();
        passengerAges.clear();
        passengerGenders.clear();

        int numAdults = getIntFromEditText(editTextAdults);
        int numChildren = getIntFromEditText(editTextChildren);
        int totalPassengers = numAdults + numChildren;

        if (totalPassengers <= 0) return;

        for (int i = 1; i <= totalPassengers; i++) {
            LinearLayout passengerLayout = new LinearLayout(this);
            passengerLayout.setOrientation(LinearLayout.VERTICAL);
            passengerLayout.setPadding(0, 10, 0, 10);

            EditText nameField = new EditText(this);
            nameField.setHint("Passenger " + i + " Name");
            passengerNames.add(nameField);

            EditText ageField = new EditText(this);
            ageField.setHint("Age");
            ageField.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            passengerAges.add(ageField);

            RadioGroup genderGroup = new RadioGroup(this);
            genderGroup.setOrientation(RadioGroup.HORIZONTAL);
            RadioButton maleButton = new RadioButton(this);
            maleButton.setText("Male");
            RadioButton femaleButton = new RadioButton(this);
            femaleButton.setText("Female");
            RadioButton otherButton = new RadioButton(this);
            otherButton.setText("Other");

            genderGroup.addView(maleButton);
            genderGroup.addView(femaleButton);
            genderGroup.addView(otherButton);
            passengerGenders.add(genderGroup);

            passengerLayout.addView(nameField);
            passengerLayout.addView(ageField);
            passengerLayout.addView(genderGroup);
            passengerContainer.addView(passengerLayout);
        }
    }

    private void savePassengerDetails() {
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (!validateInputs(email, phone)) return;

        List<String> names = new ArrayList<>();
        List<Integer> ages = new ArrayList<>();
        List<String> genders = new ArrayList<>();

        int totalPassengers = passengerNames.size();
        StringBuilder passengerDetailsString = new StringBuilder();

        for (int i = 0; i < totalPassengers; i++) {
            String name = passengerNames.get(i).getText().toString().trim();
            String ageStr = passengerAges.get(i).getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr)) {
                Toast.makeText(this, "Please fill all passenger details!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioGroup genderGroup = passengerGenders.get(i);
            int selectedId = genderGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Select gender for all passengers!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedButton = findViewById(selectedId);
            String gender = selectedButton.getText().toString();

            names.add(name);
            ages.add(Integer.parseInt(ageStr));
            genders.add(gender);

            passengerDetailsString.append("Name: ").append(name)
                    .append(", Age: ").append(ageStr)
                    .append(", Gender: ").append(gender).append("\n");
        }

        boolean isInserted = dbHelper.insertPassengerDetails(names, ages, genders, email, phone);

        if (!isInserted) {
            Toast.makeText(this, "Failed to save passenger details!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(PassengerDetailsActivity.this, PaymentActivity.class);
        intent.putExtra("FlightPrice", selectedFlight.getPrice());
        intent.putExtra("numPassengers", totalPassengers);
        intent.putExtra("searchedFlightDetails", searchedFlightDetails);
        intent.putExtra("selectedFlight", selectedFlight);
        intent.putStringArrayListExtra("selectedSeats", selectedSeats);
        intent.putExtra("selectedMeal", selectedMeal);
        intent.putExtra("passengerDetails", passengerDetailsString.toString());

        startActivity(intent);
        finish();
    }

    private boolean validateInputs(String email, String phone) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && phone.matches("\\d{10}");
    }

    private int getIntFromEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }
}