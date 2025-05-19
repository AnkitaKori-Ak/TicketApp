package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BookingConfirmationActivity extends AppCompatActivity {

    private TextView textViewSearchFlight, textViewFlightDetails, textViewSeatDetails, textViewMealDetails,
            textViewPassengerDetails, textViewPaymentInfo, textViewTotalAmount, textViewBookingReference;
    private Button buttonDownloadTicket, buttonBackToHome;
    private String searchedFlight, mealDetails, passengerDetails, paymentMethod, bookingReferenceId;
    private FlightModel selectedFlight;
    private ArrayList<String> selectedSeats;
    private int totalAmount;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        dbHelper = new DatabaseHelper(this);

        // ✅ Initialize UI Components
        textViewSearchFlight = findViewById(R.id.textViewSearchFlight);
        textViewFlightDetails = findViewById(R.id.textViewFlightDetails);
        textViewSeatDetails = findViewById(R.id.textViewSeatDetails);
        textViewMealDetails = findViewById(R.id.textViewMealDetails);
        textViewPassengerDetails = findViewById(R.id.textViewPassengerDetails);
        textViewPaymentInfo = findViewById(R.id.textViewPaymentInfo);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        textViewBookingReference = findViewById(R.id.textViewBookingReference);
        buttonDownloadTicket = findViewById(R.id.buttonDownloadTicket);
        buttonBackToHome = findViewById(R.id.buttonBackToHome);

        // ✅ Fetch Data from Intent
        searchedFlight = getIntent().getStringExtra("searchedFlightDetails");
        selectedFlight = getIntent().getParcelableExtra("selectedFlight");
        selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");
        mealDetails = getIntent().getStringExtra("selectedMeal");
        passengerDetails = getIntent().getStringExtra("passengerDetails");
        paymentMethod = getIntent().getStringExtra("paymentMethod");
        totalAmount = getIntent().getIntExtra("totalAmount", 0);

        // ✅ Generate and Display Booking Reference ID
        bookingReferenceId = generateBookingReferenceId();
        textViewBookingReference.setText("Booking Reference ID: " + bookingReferenceId);

        // ✅ Display Data (Handle null values to avoid crashes)
        textViewSearchFlight.setText("Searched Flight: " + (searchedFlight != null ? searchedFlight : "N/A"));
        textViewFlightDetails.setText(selectedFlight != null ?
                "Flight: " + selectedFlight.getAirline() +
                        "\nTime: " + selectedFlight.getDepartureTime() + " - " + selectedFlight.getArrivalTime() +
                        "\nPrice: ₹" + selectedFlight.getPrice()
                : "Flight Details: N/A");

        textViewSeatDetails.setText("Selected Seats: " + (selectedSeats != null ? selectedSeats.toString() : "N/A"));
        textViewMealDetails.setText("Meal Preference: " + (mealDetails != null ? mealDetails : "N/A"));
        textViewPassengerDetails.setText("Passenger Details:\n" + (passengerDetails != null ? passengerDetails : "N/A"));
        textViewPaymentInfo.setText("Payment Method: " + (paymentMethod != null ? paymentMethod : "N/A"));
        textViewTotalAmount.setText("Total Amount Paid: ₹" + totalAmount);

        // ✅ Handle "Download Ticket" Button Click
        buttonDownloadTicket.setOnClickListener(v -> generateTicketPDF());

        // ✅ Handle "Back to Home" Button Click
        buttonBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(BookingConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // ✅ Store Booking Details in Database
        boolean isInserted = dbHelper.insertBookingDetails(
                bookingReferenceId, searchedFlight, selectedFlight != null ? selectedFlight.getAirline() : "N/A",
                selectedSeats != null ? selectedSeats.toString() : "N/A", mealDetails, passengerDetails,
                paymentMethod, totalAmount
        );

        if (isInserted) {
            Log.d("BookingConfirmation", "Booking saved successfully in database.");
        } else {
            Log.e("BookingConfirmation", "Error saving booking in database.");
        }

    }

    // ✅ Generate Unique Booking Reference ID
    private String generateBookingReferenceId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder referenceId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            referenceId.append(characters.charAt(random.nextInt(characters.length())));
        }
        return referenceId.toString();
    }

    private void generateTicketPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 850, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        int yPos = 50;

        // ✅ **Title**
        paint.setTextSize(22);
        paint.setFakeBoldText(true);
        canvas.drawText("Flight Ticket", 150, yPos, paint);
        paint.setTextSize(14);
        paint.setFakeBoldText(false);

        // ✅ **Booking Reference ID**
        yPos += 40;
        paint.setFakeBoldText(true);
        canvas.drawText("● Booking Reference ID:", 50, yPos, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(bookingReferenceId, 80, yPos + 20, paint);
        yPos += 40;

        // ✅ **Flight Details**
        paint.setFakeBoldText(true);
        canvas.drawText("● Searched Flight:", 50, yPos, paint);
        paint.setFakeBoldText(false);
        canvas.drawText((searchedFlight != null ? searchedFlight : "N/A"), 80, yPos + 20, paint);
        yPos += 40;

        paint.setFakeBoldText(true);
        canvas.drawText("● Flight Details:", 50, yPos, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(selectedFlight != null ?
                "Flight: " + selectedFlight.getAirline() +
                        "\nTime: " + selectedFlight.getDepartureTime() + " - " + selectedFlight.getArrivalTime() +
                        "\nPrice: ₹" + selectedFlight.getPrice()
                : "Flight Details: N/A", 80, yPos + 20, paint);
        yPos += 80;

        // ✅ **Seats & Meal**
        paint.setFakeBoldText(true);
        canvas.drawText("● Selected Seats:", 50, yPos, paint);
        paint.setFakeBoldText(false);
        canvas.drawText((selectedSeats != null ? selectedSeats.toString() : "N/A"), 80, yPos + 20, paint);
        yPos += 40;

        paint.setFakeBoldText(true);
        canvas.drawText("● Meal Preference:", 50, yPos, paint);
        paint.setFakeBoldText(false);
        canvas.drawText((mealDetails != null ? mealDetails : "N/A"), 80, yPos + 20, paint);
        yPos += 40;

        // ✅ **Passenger Details**
        paint.setFakeBoldText(true);
        canvas.drawText("● Passenger Details:", 50, yPos, paint);
        yPos += 20;
        paint.setFakeBoldText(false);
        if (passengerDetails != null) {
            for (String passenger : passengerDetails.split("\n")) {
                canvas.drawText("  - " + passenger, 80, yPos, paint);
                yPos += 20;
            }
        } else {
            canvas.drawText("N/A", 80, yPos, paint);
            yPos += 20;
        }

        // ✅ **Payment Info**
        paint.setFakeBoldText(true);
        yPos += 20;
        canvas.drawText("● Payment Method:", 50, yPos, paint);
        paint.setFakeBoldText(false);
        canvas.drawText((paymentMethod != null ? paymentMethod : "N/A"), 80, yPos + 20, paint);
        yPos += 40;

        paint.setFakeBoldText(true);
        canvas.drawText("● Total Amount Paid:", 50, yPos, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("₹" + totalAmount, 80, yPos + 20, paint);
        yPos += 40;

        pdfDocument.finishPage(page);

        // ✅ **Save PDF in Downloads Folder**
        File file = new File(getExternalFilesDir(null), "FlightTicket.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Ticket Downloaded:\n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("PDF Error", "Error writing PDF: " + e.getMessage());
            Toast.makeText(this, "Failed to save ticket", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }

}
