package com.example.ticketapp;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightModel implements Parcelable {
    private String airline;
    private String departureTime;
    private String arrivalTime;
    private int price;
    private int seatsAvailable;

    public FlightModel(String airline, String departureTime, String arrivalTime, int price, int seatsAvailable) {
        this.airline = airline;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.seatsAvailable = seatsAvailable;
    }

    protected FlightModel(Parcel in) {
        airline = in.readString();
        departureTime = in.readString();
        arrivalTime = in.readString();
        price = in.readInt();
        seatsAvailable = in.readInt();
    }


    public static final Creator<FlightModel> CREATOR = new Creator<FlightModel>() {
        @Override
        public FlightModel createFromParcel(Parcel in) {
            return new FlightModel(in);
        }

        @Override
        public FlightModel[] newArray(int size) {
            return new FlightModel[size];
        }
    };

    public String getAirline() { return airline; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public int getPrice() { return price; }
    public int getSeatsAvailable() { return seatsAvailable; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(airline);
        dest.writeString(departureTime);
        dest.writeString(arrivalTime);
        dest.writeInt(price);
        dest.writeInt(seatsAvailable);
    }
}

//import android.os.Parcel;
//import android.os.Parcelable;
//
//public class FlightModel implements Parcelable {
//    private String airline;
//    private String departureTime;
//    private String arrivalTime;
//    private int price;
//
//    // ✅ Constructor
//    public FlightModel(String airline, String departureTime, String arrivalTime, int price) {
//        this.airline = airline;
//        this.departureTime = departureTime;
//        this.arrivalTime = arrivalTime;
//        this.price = price;
//    }
//
//    // ✅ Parcelable Methods
//    protected FlightModel(Parcel in) {
//        airline = in.readString();
//        departureTime = in.readString();
//        arrivalTime = in.readString();
//        price = in.readInt();
//    }
//
//    public static final Creator<FlightModel> CREATOR = new Creator<FlightModel>() {
//        @Override
//        public FlightModel createFromParcel(Parcel in) {
//            return new FlightModel(in);
//        }
//
//        @Override
//        public FlightModel[] newArray(int size) {
//            return new FlightModel[size];
//        }
//    };
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(airline);
//        dest.writeString(departureTime);
//        dest.writeString(arrivalTime);
//        dest.writeInt(price);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    // ✅ Getters
//    public String getAirline() { return airline; }
//    public String getDepartureTime() { return departureTime; }
//    public String getArrivalTime() { return arrivalTime; }
//    public int getPrice() { return price; }
//}
