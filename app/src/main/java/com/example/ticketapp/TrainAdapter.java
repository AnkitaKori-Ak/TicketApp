package com.example.ticketapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {

    private List<TrainModel> trainList;
    private OnTrainSelectListener listener;

    public interface OnTrainSelectListener {
        void onTrainSelected(TrainModel train);
    }

    public TrainAdapter(List<TrainModel> trainList, OnTrainSelectListener listener) {
        this.trainList = trainList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train, parent, false);
        return new TrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
        TrainModel train = trainList.get(position);
        holder.textTrainName.setText(train.getTrainName());
        holder.textTrainTime.setText(train.getDepartureTime() + " - " + train.getArrivalTime());
        holder.textTrainPrice.setText("₹" + train.getPrice());
        holder.textTrainSeats.setText("Seats Available: " + train.getAvailableSeats());

        holder.buttonSelect.setOnClickListener(v -> listener.onTrainSelected(train));
    }

    @Override
    public int getItemCount() {
        return trainList.size();
    }

    public static class TrainViewHolder extends RecyclerView.ViewHolder {
        TextView textTrainName, textTrainTime, textTrainPrice, textTrainSeats;
        Button buttonSelect;

        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            textTrainName = itemView.findViewById(R.id.textTrainName);
            textTrainTime = itemView.findViewById(R.id.textTrainTime);
            textTrainPrice = itemView.findViewById(R.id.textTrainPrice);
            textTrainSeats = itemView.findViewById(R.id.textTrainSeats);
            buttonSelect = itemView.findViewById(R.id.buttonSelect);
        }
    }
}
