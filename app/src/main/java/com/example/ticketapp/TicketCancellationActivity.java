package com.example.ticketapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class TicketCancellationActivity extends AppCompatActivity {

    private EditText editTextBookingRef;
    private Button buttonFindTicket, buttonCancelTicket, buttonBackToHome; // ✅ Added Back to Home Button
    private TextView textViewTicketDetails;
    private DatabaseHelper dbHelper;
    private String bookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_cancellation);

        editTextBookingRef = findViewById(R.id.editTextBookingRef);
        buttonFindTicket = findViewById(R.id.buttonFindTicket);
        buttonCancelTicket = findViewById(R.id.buttonCancelTicket);
        buttonBackToHome = findViewById(R.id.buttonBackToHome); // ✅ Initialize Back to Home Button
        textViewTicketDetails = findViewById(R.id.textViewTicketDetails);
        dbHelper = new DatabaseHelper(this);

        buttonFindTicket.setOnClickListener(v -> findTicket());
        buttonCancelTicket.setOnClickListener(v -> cancelTicket());

        // ✅ Handle "Back to Home" Button Click
        buttonBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(TicketCancellationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void findTicket() {
        bookingRef = editTextBookingRef.getText().toString().trim();

        if (bookingRef.isEmpty()) {
            Toast.makeText(this, "Enter Booking Reference!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TicketCancellation", "Searching for ticket with BookingRef: " + bookingRef);

        Cursor cursor = dbHelper.getFlightTicketDetails(bookingRef);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String flightName = cursor.getString(cursor.getColumnIndexOrThrow("selectedFlight"));
                String seatNumber = cursor.getString(cursor.getColumnIndexOrThrow("selectedSeats"));
                String mealPreference = cursor.getString(cursor.getColumnIndexOrThrow("mealPreference"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status")); // ✅ Fetch status

                Log.d("TicketCancellation", "✅ Ticket Found: " + flightName + ", Status: " + status);

                if (status.equalsIgnoreCase("Cancelled")) {
                    textViewTicketDetails.setText("Ticket is already cancelled.");
                    buttonCancelTicket.setEnabled(false);
                } else {
                    textViewTicketDetails.setText(
                            "Flight: " + flightName + "\n" +
                                    "Seat: " + seatNumber + "\n" +
                                    "Meal: " + mealPreference + "\n" +
                                    "Status: " + status); // ✅ Display status
                    buttonCancelTicket.setEnabled(true);
                }
            } catch (Exception e) {
                Log.e("TicketCancellation", "❌ Error reading ticket details: " + e.getMessage());
            } finally {
                cursor.close();
            }
        } else {
            Log.e("TicketCancellation", "❌ No ticket found for BookingRef: " + bookingRef);
            textViewTicketDetails.setText("No ticket found with this reference!");
            buttonCancelTicket.setEnabled(false);
        }
    }

    private void cancelTicket() {
        if (bookingRef == null || bookingRef.isEmpty()) {
            Toast.makeText(this, "Error: No Booking Reference!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TicketCancellation", "Attempting to cancel ticket with BookingRef: " + bookingRef);

        boolean success = dbHelper.cancelTicket(bookingRef);

        if (success) {
            Log.d("TicketCancellation", "✅ Ticket successfully cancelled.");
            Toast.makeText(this, "Ticket Cancelled Successfully!", Toast.LENGTH_SHORT).show();
            textViewTicketDetails.append("\nStatus: Cancelled");
            buttonCancelTicket.setEnabled(false);
        } else {
            Log.e("TicketCancellation", "❌ Error: Failed to cancel ticket.");
            Toast.makeText(this, "Failed to Cancel Ticket! Check Booking Reference.", Toast.LENGTH_SHORT).show();
        }
    }
}
