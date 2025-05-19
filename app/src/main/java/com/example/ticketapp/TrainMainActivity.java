package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TrainMainActivity extends AppCompatActivity {

    private Button buttonCustomerDetailsTrain, buttonSearchTrainTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_main);

        // Initialize existing buttons
        buttonCustomerDetailsTrain = findViewById(R.id.buttonCustomerDetailsTrain);
        buttonSearchTrainTicket = findViewById(R.id.buttonSearchTrainTicket);

        // Navigate to Train Customer Details
        buttonCustomerDetailsTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainMainActivity.this, RailwaysCustomerDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Train Ticket Search
        buttonSearchTrainTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainMainActivity.this, SearchTicketRailwaysActivity.class);
                startActivity(intent);
            }
        });


        Button buttonCancelTicket = findViewById(R.id.buttonCancelTicket);
        buttonCancelTicket.setOnClickListener(v -> {
            startActivity(new Intent(TrainMainActivity.this,TicketCancellationRailwaysActivity.class));
        });

        Button buttonSelectTicket = findViewById(R.id.buttonSelectTrainTicket);
        buttonSelectTicket.setOnClickListener(v-> {
            startActivity(new Intent(TrainMainActivity.this, SelectTicketsRailwaysActivity.class));
        });

        Button buttonLogout = findViewById(R.id.buttonLogout);
        if (buttonLogout != null) {
            buttonLogout.setOnClickListener(v -> {
                finishAffinity(); // ✅ Closes all activities and stops the app
                System.exit(0);   // ✅ Ensures the app process is stopped
            });
        }
        Button btnReportsMenu = findViewById(R.id.btnReportsMenu);
        if (btnReportsMenu != null) {
            btnReportsMenu.setOnClickListener(v -> {
                Intent intent = new Intent(TrainMainActivity.this, ReportMenuTrain.class);
                startActivity(intent);
            });
        }
    }
}