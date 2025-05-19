package com.example.ticketapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BookingConfirmationRailwaysActivity extends AppCompatActivity {

    private TextView textViewTrainDetails, textViewPassengerInfo, textViewSeatNumbers, textViewTotalFare, textViewPNR, textViewPaymentMethod;
    private Button buttonDownloadTicket, buttonBackToMain;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation_railways);

        // Initialize views
        textViewTrainDetails = findViewById(R.id.textViewTrainDetails);
        textViewPassengerInfo = findViewById(R.id.textViewPassengerInfo);
        textViewSeatNumbers = findViewById(R.id.textViewSeatNumbers);
        textViewTotalFare = findViewById(R.id.textViewTotalFare);
        textViewPNR = findViewById(R.id.textViewPNR);
        textViewPaymentMethod = findViewById(R.id.textViewPaymentMethod);
        buttonDownloadTicket = findViewById(R.id.buttonDownloadTicket);
        buttonBackToMain = findViewById(R.id.buttonBackToMain); // New button to go back
        dbHelper = new DatabaseHelper(this);

        // Receive Data from Intent
//        Intent intent = getIntent();
//        if (intent != null) {
//            String trainDetails = intent.getStringExtra("TRAIN_DETAILS");
//            String passengerInfo = intent.getStringExtra("PASSENGER_INFO");
//            String seatNumbers = intent.getStringExtra("SEAT_NUMBERS");
//            String totalAmount = intent.getStringExtra("TOTAL_AMOUNT");
//            String pnrNumber = intent.getStringExtra("PNR_NUMBER");
//            String paymentMethod = intent.getStringExtra("PAYMENT_METHOD");
//
//            // Display Data on Screen
//            textViewTrainDetails.setText("Train Details: " + trainDetails);
//            textViewPassengerInfo.setText("Passenger Info: " + passengerInfo);
//            textViewSeatNumbers.setText("Seat Numbers: " + seatNumbers);
//            textViewTotalFare.setText("Total Fare: ₹" + totalAmount);
//            textViewPNR.setText("PNR Number: " + pnrNumber);
//            textViewPaymentMethod.setText("Payment Method: " + paymentMethod);
        // ✅ Receive Data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            String trainDetails = intent.getStringExtra("TRAIN_DETAILS");
            String passengerInfo = intent.getStringExtra("PASSENGER_INFO"); // Fix: Make sure it's correct
            String seatNumbers = intent.getStringExtra("SEAT_NUMBERS");
            String totalAmount = intent.getStringExtra("TOTAL_AMOUNT");
            String pnrNumber = intent.getStringExtra("PNR_NUMBER");
            String paymentMethod = intent.getStringExtra("PAYMENT_METHOD");

            // ✅ Debugging to Check if Data is Null
            Log.d("BookingConfirmation", "Train Details: " + trainDetails);
            Log.d("BookingConfirmation", "Passenger Info: " + passengerInfo);
            Log.d("BookingConfirmation", "Seat Numbers: " + seatNumbers);
            Log.d("BookingConfirmation", "Total Amount: " + totalAmount);
            Log.d("BookingConfirmation", "PNR Number: " + pnrNumber);
            Log.d("BookingConfirmation", "Payment Method: " + paymentMethod);

            if (trainDetails == null || passengerInfo == null || seatNumbers == null || totalAmount == null || pnrNumber == null || paymentMethod == null) {
                Log.e("BookingConfirmation", "❌ Missing Data!");
                Toast.makeText(this, "Error: Missing Data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Set Data to UI
            textViewTrainDetails.setText("Train Details: " + trainDetails);
            textViewPassengerInfo.setText("Passenger Info: " + passengerInfo);
            textViewSeatNumbers.setText("Seat Numbers: " + seatNumbers);
            textViewTotalFare.setText("Total Fare: ₹" + totalAmount);
            textViewPNR.setText("PNR Number: " + pnrNumber);
            textViewPaymentMethod.setText("Payment Method: " + paymentMethod);



        // Store booking details in database
            boolean bookingSaved = dbHelper.insertTrainBooking(trainDetails, passengerInfo, seatNumbers, totalAmount, pnrNumber, paymentMethod);
            if (!bookingSaved) {
                Log.e("DB_ERROR", "Error inserting booking details into the database");
                Toast.makeText(this, "Error saving booking details!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("DB_SUCCESS", "Booking details inserted successfully");
            }
        } else {
            Toast.makeText(this, "Error: Missing Data!", Toast.LENGTH_SHORT).show();
        }

        // Download Ticket Button Click
        buttonDownloadTicket.setOnClickListener(v -> generateTicketPDF());

        // Back to Main Page Button Click
        buttonBackToMain.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, TrainMainActivity.class); // Change to your Train Main Activity
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });
    }

    private void generateTicketPDF() {
        // Get Data
        String trainDetails = textViewTrainDetails.getText().toString().replace("Train Details: ", "");
        String passengerInfo = textViewPassengerInfo.getText().toString().replace("Passenger Info: ", "");
        String seatNumbers = textViewSeatNumbers.getText().toString().replace("Seat Numbers: ", "");
        String totalAmount = textViewTotalFare.getText().toString().replace("Total Fare: ₹", "");
        String pnrNumber = textViewPNR.getText().toString().replace("PNR Number: ", "");
        String paymentMethod = textViewPaymentMethod.getText().toString().replace("Payment Method: ", "");

        // Create PDF
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Format and Draw text with bullet points
        int y = 50;
        canvas.drawText("Railway Ticket", 20, y, paint);
        y += 40;
        canvas.drawText("• Train Details: " + trainDetails, 20, y, paint);
        y += 30;
        canvas.drawText("• Passenger Info: " + passengerInfo, 20, y, paint);
        y += 30;
        canvas.drawText("• Seat Numbers: " + seatNumbers, 20, y, paint);
        y += 30;
        canvas.drawText("• Total Fare: ₹" + totalAmount, 20, y, paint);
        y += 30;
        canvas.drawText("• PNR Number: " + pnrNumber, 20, y, paint);
        y += 30;
        canvas.drawText("• Payment Method: " + paymentMethod, 20, y, paint);

        pdfDocument.finishPage(page);

        // Save PDF
        File file = new File(getExternalFilesDir(null), "RailwayTicket.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Ticket Downloaded: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d("BookingConfirmation", "PDF saved at: " + file.getAbsolutePath());
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save ticket", Toast.LENGTH_SHORT).show();
            Log.e("BookingConfirmation", "PDF Save Error: " + e.getMessage());
        } finally {
            pdfDocument.close();
        }
    }
}
