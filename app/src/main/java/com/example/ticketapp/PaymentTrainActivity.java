package com.example.ticketapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentTrainActivity extends AppCompatActivity {

    private TextView textViewTotalAmount;
    private RadioGroup radioGroupPaymentMethod;
    private RadioButton radioCard, radioUPI;
    private EditText editTextCardNumber, editTextExpiryDate, editTextCVV, editTextUPI;
    private Button buttonPayNow;
    private DatabaseHelper dbHelper;
    private String trainDetails, passengerDetails, seatNumbers, totalAmount, departure, arrival, travelDate, classPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_train);

        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
        radioCard = findViewById(R.id.radioCard);
        radioUPI = findViewById(R.id.radioUPI);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextUPI = findViewById(R.id.editTextUPI);
        buttonPayNow = findViewById(R.id.buttonPayNow);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        trainDetails = intent.getStringExtra("TRAIN_DETAILS");
        passengerDetails = intent.getStringExtra("PASSENGER_DETAILS");
        seatNumbers = intent.getStringExtra("SEAT_NUMBERS");
        totalAmount = intent.getStringExtra("TOTAL_AMOUNT");
        departure = intent.getStringExtra("departure");
        arrival = intent.getStringExtra("arrival");
        travelDate = intent.getStringExtra("travelDate");
        classPreference = intent.getStringExtra("classPreference");

        if (trainDetails == null || passengerDetails == null || seatNumbers == null || totalAmount == null) {
            Toast.makeText(this, "Error: Missing booking details!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        textViewTotalAmount.setText("Total Amount: ₹" + totalAmount);

        radioGroupPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCard) {
                showCardFields();
            } else if (checkedId == R.id.radioUPI) {
                showUPIField();
            }
        });

        buttonPayNow.setOnClickListener(v -> processPayment());
    }

    private void showCardFields() {
        editTextCardNumber.setVisibility(View.VISIBLE);
        editTextExpiryDate.setVisibility(View.VISIBLE);
        editTextCVV.setVisibility(View.VISIBLE);
        editTextUPI.setVisibility(View.GONE);
    }

    private void showUPIField() {
        editTextCardNumber.setVisibility(View.GONE);
        editTextExpiryDate.setVisibility(View.GONE);
        editTextCVV.setVisibility(View.GONE);
        editTextUPI.setVisibility(View.VISIBLE);
    }

    private void processPayment() {
        if (radioCard.isChecked()) {
            handleCardPayment();
        } else if (radioUPI.isChecked()) {
            handleUPIPayment();
        } else {
            Toast.makeText(this, "Select a payment method!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCardPayment() {
        String cardNumber = editTextCardNumber.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();
        String cvv = editTextCVV.getText().toString().trim();

        if (!cardNumber.matches("\\d{16}")) {
            Toast.makeText(this, "Invalid card number! Must be 16 digits.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            Toast.makeText(this, "Invalid expiry date! Use MM/YY format.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cvv.matches("\\d{3}")) {
            Toast.makeText(this, "Invalid CVV! Must be 3 digits.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.isCardExists(cardNumber)) {
            Toast.makeText(this, "This card number is already used!", Toast.LENGTH_SHORT).show();
            return;
        }
        storePaymentDetails("Card", cardNumber, expiryDate, cvv, null);
    }

    private void handleUPIPayment() {
        String upiId = editTextUPI.getText().toString().trim();

        if (!upiId.matches("[a-zA-Z0-9]+@[a-zA-Z]+")) {
            Toast.makeText(this, "Invalid UPI ID! Must be in format example@upi.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.isUPIExists(upiId)) {
            Toast.makeText(this, "This UPI ID is already used!", Toast.LENGTH_SHORT).show();
            return;
        }
        storePaymentDetails("UPI", null, null, null, upiId);
    }

    private void storePaymentDetails(String paymentMethod, String cardNumber, String expiryDate, String cvv, String upiId) {
        boolean result = dbHelper.insertPaymentDetails(paymentMethod, cardNumber, expiryDate, cvv, upiId);

        if (result) {
            saveTicketDetails();
        } else {
            Toast.makeText(this, "Payment failed! Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTicketDetails() {
        boolean ticketSaved = dbHelper.insertSelectedTrainTicket(trainDetails, "10:00 AM", "2:00 PM", Integer.parseInt(totalAmount), 1);

        if (ticketSaved) {
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT pnr FROM selected_train_tickets ORDER BY id DESC LIMIT 1", null);
            String pnr = cursor.moveToFirst() ? cursor.getString(cursor.getColumnIndexOrThrow("pnr")) : "Not Available";
            cursor.close();
            navigateToBookingConfirmation(pnr);
        } else {
            Toast.makeText(this, "Error saving ticket details!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToBookingConfirmation(String pnr) {
        Intent intent = new Intent(this, BookingConfirmationRailwaysActivity.class);
        intent.putExtra("TRAIN_DETAILS", trainDetails);
        intent.putExtra("PASSENGER_INFO", passengerDetails); // Fix: It was PASSENGER_DETAILS before
        intent.putExtra("SEAT_NUMBERS", seatNumbers);
        intent.putExtra("TOTAL_AMOUNT", totalAmount);
        intent.putExtra("PNR_NUMBER", pnr);
        intent.putExtra("PAYMENT_METHOD", radioCard.isChecked() ? "Card" : "UPI");
        startActivity(intent);
        finish();
    }

}
