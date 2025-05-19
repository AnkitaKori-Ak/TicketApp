package com.example.ticketapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private List<SeatModel> seatList;
    private OnSeatSelectListener listener;

    public interface OnSeatSelectListener {
        void onSeatSelected(SeatModel seat);
    }

    public SeatAdapter(List<SeatModel> seatList, OnSeatSelectListener listener) {
        this.seatList = seatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        SeatModel seat = seatList.get(position);
        holder.textSeatNumber.setText(seat.getSeatNumber());

        if (seat.isOccupied()) {
            holder.itemView.setBackgroundResource(R.drawable.seat_occupied); // Red for occupied
        } else if (seat.isSelected()) {
            holder.itemView.setBackgroundResource(R.drawable.seat_selected); // Green for selected
        } else {
            holder.itemView.setBackgroundResource(R.drawable.seat_available); // Gray for available
        }

        holder.itemView.setOnClickListener(v -> listener.onSeatSelected(seat));
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView textSeatNumber;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            textSeatNumber = itemView.findViewById(R.id.textSeatNumber);
        }
    }
}
