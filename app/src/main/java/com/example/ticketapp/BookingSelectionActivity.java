package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BookingSelectionActivity extends AppCompatActivity {

    private Button buttonFlightBooking, buttonTrainBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_selection);

        buttonFlightBooking = findViewById(R.id.buttonFlightBooking);
        buttonTrainBooking = findViewById(R.id.buttonTrainBooking);

        // Navigate to Flight Booking Main Page
        buttonFlightBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingSelectionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Train Booking Main Page
        buttonTrainBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingSelectionActivity.this, TrainMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
