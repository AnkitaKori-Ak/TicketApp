package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportsMenu extends AppCompatActivity {

    Button btnCustomerAirRt, btnPassengerAirRt, btnBookingAirRt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        btnCustomerAirRt = findViewById(R.id.btnCustomerAirRt);
        btnPassengerAirRt = findViewById(R.id.btnselectedtrain);
        btnBookingAirRt = findViewById(R.id.btnPassengerTrainRt);

        // Set click listeners
        if (btnPassengerAirRt != null) {
            btnPassengerAirRt.setOnClickListener(v -> openReportActivity("passengers"));
        }
        if (btnCustomerAirRt != null) {
            btnCustomerAirRt.setOnClickListener(v -> openReportActivity("airways_customers"));
        }
        if (btnBookingAirRt != null) {
            btnBookingAirRt.setOnClickListener(v -> openReportActivity("bookings"));
        }
    }

    private void openReportActivity(String tableName) {
        Intent intent = new Intent(ReportsMenu.this, Reports.class);
        intent.putExtra("TABLE_NAME", tableName);
        startActivity(intent);
    }
}
