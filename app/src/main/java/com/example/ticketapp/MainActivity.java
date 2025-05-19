package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Customer Details Button
        Button buttonCustomerDetails = findViewById(R.id.buttonCustomerDetails);
        if (buttonCustomerDetails != null) {
            buttonCustomerDetails.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CustomerDetailsAirwaysActivity.class);
                startActivity(intent);
            });
        }

        // Search Ticket Button
        Button buttonSearchTicket = findViewById(R.id.buttonSearchTicket);
        if (buttonSearchTicket != null) {
            buttonSearchTicket.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SearchTicketAirwaysActivity.class);
                startActivity(intent);
            });
        }

        // Select Ticket Button
        Button buttonSelectTicket = findViewById(R.id.buttonSelectTicket);
        if (buttonSelectTicket != null) {
            buttonSelectTicket.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SelectTicketsActivity.class);
                startActivity(intent);
            });
        }

        // Cancel Ticket Button
        Button buttonCancelTicket = findViewById(R.id.buttonCancelTicket);
        if (buttonCancelTicket != null) {
            buttonCancelTicket.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, TicketCancellationActivity.class);
                startActivity(intent);
            });
        }

        // Reports Menu Button (FIXED ✅)
        Button btnReportsMenu = findViewById(R.id.btnReportsMenu);
        if (btnReportsMenu != null) {
            btnReportsMenu.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ReportsMenu.class);
                startActivity(intent);
            });
        }

        // Logout Button
        Button buttonLogout = findViewById(R.id.buttonLogout);
        if (buttonLogout != null) {
            buttonLogout.setOnClickListener(v -> {
                finishAffinity(); // ✅ Closes all activities
                System.exit(0);   // ✅ Ensures app stops completely
            });
        }
    }
}