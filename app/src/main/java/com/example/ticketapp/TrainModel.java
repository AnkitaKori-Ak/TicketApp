package com.example.ticketapp;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainModel implements Parcelable {
    private String trainName;
    private String departureTime;
    private String arrivalTime;
    private int price;
    private int availableSeats;

    public TrainModel(String trainName, String departureTime, String arrivalTime, int price, int availableSeats) {
        this.trainName = trainName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    protected TrainModel(Parcel in) {
        trainName = in.readString();
        departureTime = in.readString();
        arrivalTime = in.readString();
        price = in.readInt();
        availableSeats = in.readInt();
    }

    public static final Creator<TrainModel> CREATOR = new Creator<TrainModel>() {
        @Override
        public TrainModel createFromParcel(Parcel in) {
            return new TrainModel(in);
        }

        @Override
        public TrainModel[] newArray(int size) {
            return new TrainModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(trainName);
        parcel.writeString(departureTime);
        parcel.writeString(arrivalTime);
        parcel.writeInt(price);
        parcel.writeInt(availableSeats);
    }

    public String getTrainName() { return trainName; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public int getPrice() { return price; }
    public int getAvailableSeats() { return availableSeats; }
}
