package com.example.ticketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TicketCancellationRailwaysActivity extends AppCompatActivity {

    private EditText editTextPNR;
    private Button buttonFindTicket, buttonCancelTicket;
    private TextView textViewTicketDetails;
    private DatabaseHelper dbHelper;
    private String enteredPNR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_cancellation_railways);

        editTextPNR = findViewById(R.id.editTextPNR);
        buttonFindTicket = findViewById(R.id.buttonFindTicket);
        buttonCancelTicket = findViewById(R.id.buttonCancelTicket);
        textViewTicketDetails = findViewById(R.id.textViewTicketDetails);
        dbHelper = new DatabaseHelper(this);

        buttonFindTicket.setOnClickListener(v -> findTicket());
        buttonCancelTicket.setOnClickListener(v -> cancelTicket());
    }

    private void findTicket() {
        enteredPNR = editTextPNR.getText().toString().trim();
        if (enteredPNR.isEmpty()) {
            Toast.makeText(this, "Enter PNR Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = dbHelper.getTrainTicketByPNR(enteredPNR);
        if (cursor.moveToFirst()) {
            String trainName = cursor.getString(cursor.getColumnIndexOrThrow("train_name"));
            String departureTime = cursor.getString(cursor.getColumnIndexOrThrow("departure_time"));
            String arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow("arrival_time"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
            int numPassengers = cursor.getInt(cursor.getColumnIndexOrThrow("num_passengers"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            if (status.equals("Cancelled")) {
                textViewTicketDetails.setText("Ticket is already cancelled.");
                buttonCancelTicket.setEnabled(false);
            } else {
                textViewTicketDetails.setText(
                        "Train: " + trainName + "\n" +
                                "Time: " + departureTime + " - " + arrivalTime + "\n" +
                                "Passengers: " + numPassengers + "\n" +
                                "Total Fare: ₹" + price + "\n" +
                                "Status: " + status);
                buttonCancelTicket.setEnabled(true);
            }
        } else {
            textViewTicketDetails.setText("No ticket found with this PNR!");
            buttonCancelTicket.setEnabled(false);
        }
        cursor.close();
    }

    private void cancelTicket() {
        if (dbHelper.cancelTrainTicket(enteredPNR)) {
            Toast.makeText(this, "Ticket Cancelled Successfully!", Toast.LENGTH_SHORT).show();
            textViewTicketDetails.append("\nStatus: Cancelled");
            buttonCancelTicket.setEnabled(false);
        } else {
            Toast.makeText(this, "Failed to Cancel Ticket!", Toast.LENGTH_SHORT).show();
        }
    }
}
