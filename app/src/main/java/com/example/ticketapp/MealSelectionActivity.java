package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MealSelectionActivity extends AppCompatActivity {

    private RadioGroup radioGroupMeal;
    private Button buttonConfirmMeal;
    private String selectedMeal = "Vegetarian"; // ✅ Default selection
    private FlightModel selectedFlight;
    private ArrayList<String> selectedSeats; // ✅ Corrected to use String ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_selection);

        // Initialize UI elements
        radioGroupMeal = findViewById(R.id.radioGroupMeal);
        buttonConfirmMeal = findViewById(R.id.buttonConfirmMeal);

        // Retrieve Intent data
        selectedFlight = getIntent().getParcelableExtra("selectedFlight");
        selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");

        // Log received data for debugging
        Log.d("MealSelectionActivity", "Received selectedFlight: " + selectedFlight);
        Log.d("MealSelectionActivity", "Received selectedSeats: " + selectedSeats);

        if (selectedFlight == null || selectedSeats == null || selectedSeats.isEmpty()) {
            Toast.makeText(this, "Error: Missing flight or seat data!", Toast.LENGTH_SHORT).show();
            Log.e("MealSelectionActivity", "Error: Missing flight or seat data!");
            finish();
            return;
        }

        // Handle meal selection
        radioGroupMeal.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            if (selectedRadioButton != null) {
                selectedMeal = selectedRadioButton.getText().toString();
            }
        });

        // Confirm Meal Selection Button Click
        buttonConfirmMeal.setOnClickListener(v -> {
            if (radioGroupMeal.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select a meal!", Toast.LENGTH_SHORT).show();
                Log.e("MealSelectionActivity", "Error: No meal selected!");
                return;
            }

            Log.d("MealSelectionActivity", "Selected Meal: " + selectedMeal);

            // ✅ Ensure all data is passed correctly to ReviewTicketActivity
            Intent intent = new Intent(MealSelectionActivity.this, ReviewTicketActivity.class);
            intent.putExtra("searchedFlightDetails", getIntent().getStringExtra("searchedFlightDetails"));
            intent.putExtra("selectedFlight", selectedFlight); // ✅ Pass the flight object
            intent.putStringArrayListExtra("selectedSeats", selectedSeats); // ✅ Correctly pass selectedSeats
            intent.putExtra("selectedMeal", selectedMeal);

            Log.d("MealSelectionActivity", "Starting ReviewTicketActivity with selectedMeal: " + selectedMeal);
            startActivity(intent);
        });
    }
}
