package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ReviewTicketActivity extends AppCompatActivity {

    private TextView textViewFlightDetails, textViewSeatDetails, textViewMealDetails;
    private Button buttonConfirmBooking;
    private FlightModel selectedFlight;
    private ArrayList<String> selectedSeats;
    private String searchedFlightDetails, selectedMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_ticket);

        textViewFlightDetails = findViewById(R.id.textViewFlightDetails);
        textViewSeatDetails = findViewById(R.id.textViewSeatDetails);
        textViewMealDetails = findViewById(R.id.textViewMealDetails);
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking);

        // ✅ Get Data from Intent
        selectedFlight = getIntent().getParcelableExtra("selectedFlight");
        searchedFlightDetails = getIntent().getStringExtra("searchedFlightDetails");
        selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");
        selectedMeal = getIntent().getStringExtra("selectedMeal");

        // ✅ Log received data
        Log.d("ReviewTicketActivity", "selectedFlight: " + selectedFlight);
        Log.d("ReviewTicketActivity", "searchedFlightDetails: " + searchedFlightDetails);
        Log.d("ReviewTicketActivity", "selectedSeats: " + selectedSeats);
        Log.d("ReviewTicketActivity", "selectedMeal: " + selectedMeal);

        if (selectedFlight == null || searchedFlightDetails == null || selectedSeats == null || selectedSeats.isEmpty() || selectedMeal == null) {
            Log.e("ReviewTicketActivity", "Error: Missing data!");
            Toast.makeText(this, "Error: Missing ticket data!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ Display Data
        textViewFlightDetails.setText("Flight: " + selectedFlight.getAirline() +
                "\nTime: " + selectedFlight.getDepartureTime() + " - " + selectedFlight.getArrivalTime() +
                "\nPrice: ₹" + selectedFlight.getPrice());

        textViewSeatDetails.setText("Selected Seats: " + String.join(", ", selectedSeats));
        textViewMealDetails.setText("Meal Preference: " + selectedMeal);

        // ✅ Button Click Listener
        buttonConfirmBooking.setOnClickListener(v -> {
            Intent intent = new Intent(ReviewTicketActivity.this, PassengerDetailsActivity.class);
            intent.putExtra("selectedFlight", selectedFlight); // ✅ Pass as Parcelable
            intent.putExtra("searchedFlightDetails", searchedFlightDetails);
            intent.putStringArrayListExtra("selectedSeats", selectedSeats);
            intent.putExtra("selectedMeal", selectedMeal);
            startActivity(intent);
        });
    }
}
