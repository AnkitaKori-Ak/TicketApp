package com.example.ticketapp;

import android.os.Parcel;
import android.os.Parcelable;

public class SeatModel implements Parcelable {
    private String seatNumber;
    private boolean isSelected;
    private boolean isOccupied;

    public SeatModel(String seatNumber, boolean isSelected, boolean isOccupied) {
        this.seatNumber = seatNumber;
        this.isSelected = isSelected;
        this.isOccupied = isOccupied;
    }

    protected SeatModel(Parcel in) {
        seatNumber = in.readString();
        isSelected = in.readByte() != 0;
        isOccupied = in.readByte() != 0;
    }

    public static final Creator<SeatModel> CREATOR = new Creator<SeatModel>() {
        @Override
        public SeatModel createFromParcel(Parcel in) {
            return new SeatModel(in);
        }

        @Override
        public SeatModel[] newArray(int size) {
            return new SeatModel[size];
        }
    };

    public String getSeatNumber() { return seatNumber; }
    public boolean isSelected() { return isSelected; }
    public boolean isOccupied() { return isOccupied; }

    public void setSelected(boolean selected) { isSelected = selected; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seatNumber);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isOccupied ? 1 : 0));
    }
}
