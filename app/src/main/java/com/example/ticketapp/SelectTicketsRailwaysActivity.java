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

public class SelectTicketsRailwaysActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTrains;
    private Button buttonProceed;
    private TrainAdapter trainAdapter;
    private List<TrainModel> trainList;
    private TrainModel selectedTrain;

    // ✅ Variables to hold search details
    private String departure, arrival, travelDate, classPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tickets_railways);

        recyclerViewTrains = findViewById(R.id.recyclerViewTrains);
        buttonProceed = findViewById(R.id.buttonProceed);
        buttonProceed.setEnabled(false); // Initially disabled

        // ✅ Receive Search Details from Intent
        Intent intent = getIntent();
        departure = intent.getStringExtra("departure");
        arrival = intent.getStringExtra("arrival");
        travelDate = intent.getStringExtra("travelDate");
        classPreference = intent.getStringExtra("classPreference");

        // ✅ Log received details for debugging
        Toast.makeText(this, "From: " + departure + " To: " + arrival +
                "\nDate: " + travelDate + " Class: " + classPreference, Toast.LENGTH_LONG).show();

        // Dummy train data
        trainList = new ArrayList<>();
        trainList.add(new TrainModel("Rajdhani Express", "06:00 AM", "02:00 PM", 1500, 50));
        trainList.add(new TrainModel("Shatabdi Express", "08:00 AM", "04:00 PM", 1800, 40));
        trainList.add(new TrainModel("Duronto Express", "10:00 AM", "06:00 PM", 1600, 30));

        trainAdapter = new TrainAdapter(trainList, train -> {
            selectedTrain = train;
            Toast.makeText(this, "Selected: " + train.getTrainName(), Toast.LENGTH_SHORT).show();
            buttonProceed.setEnabled(true);
        });

        recyclerViewTrains.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrains.setAdapter(trainAdapter);

        // ✅ Proceed button click logic
        buttonProceed.setOnClickListener(v -> {
            if (selectedTrain != null) {
                // ✅ Pass selected train + search details to PassengerDetailsRailwaysActivity
                Intent passengerIntent = new Intent(SelectTicketsRailwaysActivity.this, PassengerDetailsRailwaysActivity.class);
                passengerIntent.putExtra("selectedTrain", selectedTrain);
                passengerIntent.putExtra("departure", departure);
                passengerIntent.putExtra("arrival", arrival);
                passengerIntent.putExtra("travelDate", travelDate);
                passengerIntent.putExtra("classPreference", classPreference);
                startActivity(passengerIntent);
            } else {
                Toast.makeText(this, "Please select a train!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
