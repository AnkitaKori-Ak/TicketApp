package com.example.ticketapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<FlightModel> flightList;
    private OnFlightSelectListener listener;

    public interface OnFlightSelectListener {
        void onFlightSelected(FlightModel flight);
    }

    public FlightAdapter(List<FlightModel> flightList, OnFlightSelectListener listener) {
        this.flightList = flightList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        FlightModel flight = flightList.get(position);

        if (flight != null) {
            holder.textAirline.setText(flight.getAirline());
            holder.textTime.setText(flight.getDepartureTime() + " - " + flight.getArrivalTime());
            holder.textPrice.setText("₹" + flight.getPrice());
            holder.textSeats.setText("Seats Available: " + flight.getSeatsAvailable());

            holder.buttonSelect.setOnClickListener(v -> listener.onFlightSelected(flight));
        }
    }

    @Override
    public int getItemCount() {
        return (flightList != null) ? flightList.size() : 0;
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView textAirline, textTime, textPrice, textSeats;
        Button buttonSelect;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textAirline = itemView.findViewById(R.id.textAirline);
            textTime = itemView.findViewById(R.id.textTime);
            textPrice = itemView.findViewById(R.id.textPrice);
            textSeats = itemView.findViewById(R.id.textSeats);
            buttonSelect = itemView.findViewById(R.id.buttonSelect);
        }
    }
}
