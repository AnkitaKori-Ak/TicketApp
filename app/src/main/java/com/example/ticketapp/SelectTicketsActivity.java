package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SelectTicketsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFlights;
    private Button buttonProceed;
    private FlightAdapter flightAdapter;
    private List<FlightModel> flightList;
    private FlightModel selectedFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tickets);

        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        buttonProceed = findViewById(R.id.buttonProceed);
        buttonProceed.setEnabled(false);

        flightList = new ArrayList<>();
        flightList.add(new FlightModel("IndiGo", "08:00 AM", "10:00 AM", 4500, 20));
        flightList.add(new FlightModel("Air India", "12:00 PM", "02:30 PM", 5500, 15));
        flightList.add(new FlightModel("SpiceJet", "05:00 PM", "07:15 PM", 5000, 10));

        flightAdapter = new FlightAdapter(flightList, flight -> {
            selectedFlight = flight;
            buttonProceed.setEnabled(true);
        });

        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFlights.setAdapter(flightAdapter);

        buttonProceed.setOnClickListener(v -> {
            if (selectedFlight != null) {
                Intent intent = new Intent(SelectTicketsActivity.this, SeatSelectionActivity.class);
                intent.putExtra("searchedFlightDetails", getIntent().getStringExtra("searchedFlightDetails"));
                intent.putExtra("selectedFlight", selectedFlight); // ✅ Pass the FlightModel object
                startActivity(intent);
            }
        });
    }
}
