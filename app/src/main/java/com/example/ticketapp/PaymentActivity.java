package com.example.ticketapp;

import android.content.Intent;
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
import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private TextView textViewTotalAmount;
    private RadioGroup radioGroupPaymentMethod;
    private EditText editTextCardNumber, editTextExpiryDate, editTextCVV, editTextUPI;
    private View layoutCardDetails;
    private Button buttonPay;
    private String selectedPaymentMethod = "Credit/Debit Card";
    private int totalAmount, flightPrice, numPassengers;
    private String searchedFlightDetails, selectedMeal, passengerDetails;
    private FlightModel selectedFlight;
    private ArrayList<String> selectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextUPI = findViewById(R.id.editTextUPI);
        layoutCardDetails = findViewById(R.id.layoutCardDetails);
        buttonPay = findViewById(R.id.buttonPay);

        // ✅ Retrieve Data from Intent (BEFORE validation)
        flightPrice = getIntent().getIntExtra("FlightPrice", 0);
        numPassengers = getIntent().getIntExtra("numPassengers", 1);
        searchedFlightDetails = getIntent().getStringExtra("searchedFlightDetails");
        selectedFlight = getIntent().getParcelableExtra("selectedFlight");
        selectedSeats = getIntent().getStringArrayListExtra("selectedSeats");
        selectedMeal = getIntent().getStringExtra("selectedMeal");
        passengerDetails = getIntent().getStringExtra("passengerDetails");

        // ✅ Log received data for debugging
        Log.d("PaymentActivity", "FlightPrice: " + flightPrice);
        Log.d("PaymentActivity", "NumPassengers: " + numPassengers);
        Log.d("PaymentActivity", "searchedFlightDetails: " + searchedFlightDetails);
        Log.d("PaymentActivity", "selectedFlight: " + selectedFlight);
        Log.d("PaymentActivity", "selectedSeats: " + selectedSeats);
        Log.d("PaymentActivity", "selectedMeal: " + selectedMeal);
        Log.d("PaymentActivity", "passengerDetails: " + passengerDetails);
        Log.d("PaymentActivity", "FlightPrice AFTER Intent: " + flightPrice);

        if (flightPrice <= 0) {
            Log.e("PaymentActivity", "Error: Flight price is missing or 0!");
            Toast.makeText(this, "Error: Flight price missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Ensure all required data is received before proceeding
        if (flightPrice == 0 || numPassengers == 0 || selectedFlight == null ||
                selectedSeats == null || selectedSeats.isEmpty() || selectedMeal == null || passengerDetails == null) {
            Log.e("PaymentActivity", "Error receiving booking details! One or more values are NULL.");
            Toast.makeText(this, "Error receiving booking details!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Fix: Ensure `totalAmount` is properly calculated
        totalAmount = flightPrice * numPassengers;
        textViewTotalAmount.setText("Total Amount: ₹" + totalAmount);



    // ✅ Handle Payment Method Selection
        radioGroupPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            layoutCardDetails.setVisibility(View.GONE);
            editTextUPI.setVisibility(View.GONE);

            RadioButton selectedRadio = findViewById(checkedId);
            selectedPaymentMethod = selectedRadio.getText().toString();

            if (selectedPaymentMethod.equals("Credit/Debit Card")) {
                layoutCardDetails.setVisibility(View.VISIBLE);
            } else if (selectedPaymentMethod.equals("UPI")) {
                editTextUPI.setVisibility(View.VISIBLE);
            }
        });

        // ✅ Payment Button Click
        buttonPay.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        if (selectedPaymentMethod.equals("Credit/Debit Card")) {
            String cardNumber = editTextCardNumber.getText().toString().trim();
            String expiryDate = editTextExpiryDate.getText().toString().trim();
            String cvv = editTextCVV.getText().toString().trim();

            if (!validateCardDetails(cardNumber, expiryDate, cvv)) {
                return;
            }
        } else if (selectedPaymentMethod.equals("UPI")) {
            String upiId = editTextUPI.getText().toString().trim();

            if (!validateUPI(upiId)) {
                return;
            }
        }

        // ✅ Log Payment Details
        Log.d("PaymentActivity", "Proceeding with payment...");
        Log.d("PaymentActivity", "Payment Method: " + selectedPaymentMethod);
        Log.d("PaymentActivity", "Total Amount: " + totalAmount);

        // ✅ Navigate to Booking Confirmation
        Intent intent = new Intent(PaymentActivity.this, BookingConfirmationActivity.class);
        intent.putExtra("searchedFlightDetails", searchedFlightDetails);
        intent.putExtra("selectedFlight", selectedFlight);
        intent.putStringArrayListExtra("selectedSeats", selectedSeats);
        intent.putExtra("selectedMeal", selectedMeal);
        intent.putExtra("passengerDetails", passengerDetails);
        intent.putExtra("paymentMethod", selectedPaymentMethod);
        intent.putExtra("totalAmount", totalAmount);
        startActivity(intent);
        finish();
    }

    // ✅ Card Validation
    private boolean validateCardDetails(String cardNumber, String expiryDate, String cvv) {
        if (cardNumber.isEmpty() || cardNumber.length() != 16 || !cardNumber.matches("\\d{16}")) {
            editTextCardNumber.setError("Enter a valid 16-digit card number");
            return false;
        }
        if (expiryDate.isEmpty() || !expiryDate.matches("(0[1-9]|1[0-2])/(\\d{2})")) {
            editTextExpiryDate.setError("Enter expiry in MM/YY format");
            return false;
        }
        if (cvv.isEmpty() || cvv.length() != 3 || !cvv.matches("\\d{3}")) {
            editTextCVV.setError("Enter a valid 3-digit CVV");
            return false;
        }
        return true;
    }

    // ✅ UPI Validation
    private boolean validateUPI(String upiId) {
        if (upiId.isEmpty() || !upiId.matches("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$")) {
            editTextUPI.setError("Enter a valid UPI ID (example@upi)");
            return false;
        }
        return true;
    }
}
