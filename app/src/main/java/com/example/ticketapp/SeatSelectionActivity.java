package com.example.ticketapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeatSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSeats;
    private Button buttonConfirmSeat;
    private TextView textSelectedSeats;
    private SeatAdapter seatAdapter;
    private List<SeatModel> seatList;
    private List<SeatModel> selectedSeats;
    private FlightModel selectedFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        recyclerViewSeats = findViewById(R.id.recyclerViewSeats);
        buttonConfirmSeat = findViewById(R.id.buttonConfirmSeat);
        textSelectedSeats = findViewById(R.id.textSelectedSeats);

        selectedFlight = getIntent().getParcelableExtra("selectedFlight");

        seatList = new ArrayList<>();
        selectedSeats = new ArrayList<>();

        generateSeatsWithRandomOccupancy();

        recyclerViewSeats.setLayoutManager(new GridLayoutManager(this, 5));
        seatAdapter = new SeatAdapter(seatList, this::toggleSeatSelection);
        recyclerViewSeats.setAdapter(seatAdapter);

        buttonConfirmSeat.setOnClickListener(v -> proceedToNext());
    }

    private void generateSeatsWithRandomOccupancy() {
        Random random = new Random();
        for (int i = 1; i <= 30; i++) {
            boolean isOccupied = random.nextBoolean();
            seatList.add(new SeatModel("S" + i, false, isOccupied));
        }
    }

    private void toggleSeatSelection(SeatModel seat) {
        if (seat.isOccupied()) {
            Toast.makeText(this, "This seat is occupied!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            seat.setSelected(false);
        } else {
            selectedSeats.add(seat);
            seat.setSelected(true);
        }

        seatAdapter.notifyDataSetChanged();
        updateSeatCounter();
    }

    private void updateSeatCounter() {
        textSelectedSeats.setText("Selected Seats: " + selectedSeats.size());
        buttonConfirmSeat.setEnabled(!selectedSeats.isEmpty());
    }

    private void proceedToNext() {
        if (selectedSeats.isEmpty()) {
            Toast.makeText(this, "Please select at least one seat!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> selectedSeatList = new ArrayList<>();
        for (SeatModel seat : selectedSeats) {
            selectedSeatList.add(seat.getSeatNumber());
        }

        Intent intent = new Intent(SeatSelectionActivity.this, MealSelectionActivity.class);
        intent.putExtra("searchedFlightDetails", getIntent().getStringExtra("searchedFlightDetails"));
        intent.putExtra("selectedFlight", selectedFlight);
        intent.putStringArrayListExtra("selectedSeats", selectedSeatList);
        startActivity(intent);
    }
}
