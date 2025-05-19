package com.example.ticketapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportMenuTrain extends AppCompatActivity {

    Button btnCustomerRailRt,btnPassengerRailRt,btnBookingRailRt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_menu_train);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnCustomerRailRt = findViewById(R.id.btnCustomerRailRt);
        btnPassengerRailRt = findViewById(R.id.btnPassengerTrainRt);
        btnBookingRailRt = findViewById(R.id.btnBookingTrainRt);

        if (btnCustomerRailRt != null) {
            btnCustomerRailRt.setOnClickListener(v -> openReportActivity("railways_customer"));
        }
        if (btnPassengerRailRt != null) {
            btnPassengerRailRt.setOnClickListener(v -> openReportActivity("railways_passengers"));
        }
        if (btnBookingRailRt != null) {
            btnBookingRailRt.setOnClickListener(v -> openReportActivity("train_bookings"));
        }
    }
    private void openReportActivity(String tableName) {
        Intent intent = new Intent(ReportMenuTrain.this, Reports.class);
        intent.putExtra("TABLE_NAME", tableName);
        startActivity(intent);
    }
}