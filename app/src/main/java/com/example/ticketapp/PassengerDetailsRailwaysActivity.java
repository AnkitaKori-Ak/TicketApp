package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PassengerDetailsRailwaysActivity extends AppCompatActivity {

    private EditText edtNumAdults, edtNumChildren, edtContact;
    private Spinner spinnerSeatPreference, spinnerMealPreference;
    private Button btnProceed;
    private LinearLayout passengerContainer;

    private List<EditText> passengerNames, passengerAges;
    private List<RadioGroup> passengerGenders;

    private TrainModel selectedTrain;
    private String departure, arrival, travelDate, classPreference;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details_railways);

        // ✅ Initialize Views
        edtNumAdults = findViewById(R.id.editTextNumAdults);
        edtNumChildren = findViewById(R.id.editTextNumChildren);
        edtContact = findViewById(R.id.editTextContact);
        spinnerSeatPreference = findViewById(R.id.spinnerSeatPreference);
        spinnerMealPreference = findViewById(R.id.spinnerMealPreference);
        btnProceed = findViewById(R.id.buttonProceed);
        passengerContainer = findViewById(R.id.passengerContainer);

        dbHelper = new DatabaseHelper(this); // ✅ Properly initialized

        // ✅ Get Data from Intent
        Intent intent = getIntent();
        departure = intent.getStringExtra("departure");
        arrival = intent.getStringExtra("arrival");
        travelDate = intent.getStringExtra("travelDate");
        classPreference = intent.getStringExtra("classPreference");
        selectedTrain = intent.getParcelableExtra("selectedTrain");

        if (selectedTrain == null) {
            Toast.makeText(this, "Error: Train details not received!", Toast.LENGTH_LONG).show();
            finish();
        }

        // ✅ Populate Spinners
        setupSpinner(spinnerSeatPreference, R.array.seat_preferences);
        setupSpinner(spinnerMealPreference, R.array.meal_preferences);

        // Initialize Lists
        passengerNames = new ArrayList<>();
        passengerAges = new ArrayList<>();
        passengerGenders = new ArrayList<>();

        // Set up EditText Listeners
        edtNumAdults.setOnFocusChangeListener((v, hasFocus) -> generatePassengerFields());
        edtNumChildren.setOnFocusChangeListener((v, hasFocus) -> generatePassengerFields());

        // Proceed Button Click
        btnProceed.setOnClickListener(v -> savePassengerDetails());
    }

    // ✅ Populate Spinners
    private void setupSpinner(Spinner spinner, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayResId, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // ✅ Generate Passenger Fields Dynamically
    private void generatePassengerFields() {
        passengerContainer.removeAllViews();
        passengerNames.clear();
        passengerAges.clear();
        passengerGenders.clear();

        int numAdults = getIntFromEditText(edtNumAdults);
        int numChildren = getIntFromEditText(edtNumChildren);
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
            ageField.setInputType(InputType.TYPE_CLASS_NUMBER);
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

    // ✅ Save Passenger Details and Navigate to PaymentTrainActivity
    private void savePassengerDetails() {
        String contact = edtContact.getText().toString().trim();
        if (!validateInputs(contact)) return;

        List<String> passengerNamesList = new ArrayList<>();
        List<Integer> passengerAgesList = new ArrayList<>();
        List<String> passengerGendersList = new ArrayList<>();

        int totalPassengers = passengerNames.size();
        StringBuilder passengerDetails = new StringBuilder();

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
            passengerNamesList.add(name);
            passengerAgesList.add(Integer.parseInt(ageStr));
            passengerGendersList.add(selectedButton.getText().toString());

            passengerDetails.append("Name: ").append(name)
                    .append(", Age: ").append(ageStr)
                    .append(", Gender: ").append(selectedButton.getText().toString())
                    .append("\n");
        }

        String seatNumbers = generateSeatNumbers(totalPassengers);

        boolean isInserted = dbHelper.insertsRailwaysPassengers(
                selectedTrain.getTrainName(), departure, arrival, travelDate,
                classPreference, seatNumbers, spinnerMealPreference.getSelectedItem().toString(),
                passengerNamesList, passengerAgesList, passengerGendersList, contact
        );

        Log.d("PassengerDetailsRailways", "Passenger Details: " + passengerDetails.toString());

        if (isInserted) {
            Toast.makeText(this, "Passengers saved successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PassengerDetailsRailwaysActivity.this, PaymentTrainActivity.class);
            intent.putExtra("TOTAL_AMOUNT", String.valueOf(selectedTrain.getPrice() * totalPassengers));
            intent.putExtra("SEAT_NUMBERS", seatNumbers);
            intent.putExtra("TRAIN_DETAILS", selectedTrain.getTrainName());
            intent.putExtra("PASSENGER_DETAILS", passengerDetails.toString());
            intent.putExtra("departure", departure);
            intent.putExtra("arrival", arrival);
            intent.putExtra("travelDate", travelDate);
            intent.putExtra("classPreference", classPreference);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error saving passengers!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String contact) {
        return Patterns.PHONE.matcher(contact).matches() && contact.length() == 10;
    }


private String generateSeatNumbers(int totalPassengers) {
    Set<Integer> seatSet = new HashSet<>();
    Random random = new Random();

    while (seatSet.size() < totalPassengers) {
        seatSet.add(random.nextInt(100) + 1);
    }

    List<String> seatList = new ArrayList<>();
    for (int seat : seatSet) {
        seatList.add("S" + seat); // Add "South" before seat number
    }

    return TextUtils.join(", ", seatList);
}


    private int getIntFromEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }
}
