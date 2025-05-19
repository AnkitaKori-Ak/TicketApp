package com.example.ticketapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TicketBooking.db";
    private static final int DATABASE_VERSION = 21; // Incremented version

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "email TEXT UNIQUE, " +
                    "phone TEXT, " +
                    "password TEXT)";

    private static final String CREATE_RAILWAYS_PASSENGERS_TABLE =
            "CREATE TABLE IF NOT EXISTS railways_passengers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "train_name TEXT, " +
                    "departure TEXT, " +
                    "arrival TEXT, " +
                    "travel_date TEXT, " +
                    "class_preference TEXT, " +
                    "seat_numbers TEXT, " +
                    "meal_preference TEXT, " +
                    "passenger_name TEXT, " +
                    "age INTEGER, " +
                    "gender TEXT, " +
                    "contact TEXT)"; // Added contact info


    private static final String CREATE_SELECTED_TRAIN_TICKETS_TABLE =
            "CREATE TABLE selected_train_tickets (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "pnr TEXT UNIQUE, " +
                    "train_name TEXT, " +
                    "departure_time TEXT, " +
                    "arrival_time TEXT, " +
                    "price INTEGER, " +
                    "num_passengers INTEGER, " +
                    "status TEXT DEFAULT 'Booked')";

    private static final String CREATE_PAYMENT_TABLE =
            "CREATE TABLE payment_details (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "payment_method TEXT, " +
                    "card_number TEXT, " +
                    "expiry_date TEXT, " +
                    "cvv TEXT, " +
                    "upi_id TEXT)";

    private static final String CREATE_AIRWAYS_CUSTOMERS_TABLE =
            "CREATE TABLE IF NOT EXISTS airways_customers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "full_name TEXT, " +
                    "passport TEXT UNIQUE, " + // Unique Passport Number
                    "dob TEXT, " +
                    "contact TEXT UNIQUE)"; // Unique Contact Number

    private static final String CREATE_PASSENGERS_TABLE =
            "CREATE TABLE passengers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "age INTEGER, " +
                    "gender TEXT, " +
                    "email TEXT, " +
                    "phone TEXT)";

    private static final String TABLE_BOOKINGS = "bookings";

    private static final String CREATE_TABLE_BOOKINGS = "CREATE TABLE bookings (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "bookingReference TEXT UNIQUE, " +
            "searchedFlight TEXT, " +
            "selectedFlight TEXT, " +
            "selectedSeats TEXT, " +
            "mealPreference TEXT, " +
            "passengerDetails TEXT, " +
            "paymentMethod TEXT, " +
            "totalAmount INTEGER, " +
            "status TEXT DEFAULT 'Booked')"; // ✅ Added "status" column

    private static final String TABLE_TRAIN_BOOKINGS = "train_bookings";

    private static final String CREATE_TABLE_TRAIN_BOOKINGS = "CREATE TABLE " + TABLE_TRAIN_BOOKINGS + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "train_details TEXT, " +
            "passenger_info TEXT, " +
            "seat_numbers TEXT, " +
            "total_fare TEXT, " +
            "pnr_number TEXT UNIQUE, " +
            "payment_method TEXT, " +
            "status TEXT DEFAULT 'Booked')";

    private static final String TABLE_RAILWAYS_CUSTOMER = "railways_customer";
    private static final String CREATE_TABLE_RAILWAYS_CUSTOMER = "CREATE TABLE " + TABLE_RAILWAYS_CUSTOMER + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "full_name TEXT NOT NULL, " +
            "age INTEGER NOT NULL, " +
            "gender TEXT NOT NULL, " +
            "contact TEXT UNIQUE NOT NULL, " +
            "id_number TEXT UNIQUE NOT NULL)";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase(); // Ensure DB is opened
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_USERS_TABLE);
            db.execSQL(CREATE_RAILWAYS_PASSENGERS_TABLE);
            db.execSQL(CREATE_SELECTED_TRAIN_TICKETS_TABLE);
            db.execSQL(CREATE_PAYMENT_TABLE);
            db.execSQL(CREATE_AIRWAYS_CUSTOMERS_TABLE);
            db.execSQL(CREATE_PASSENGERS_TABLE);
            db.execSQL(CREATE_TABLE_BOOKINGS);
            db.execSQL(CREATE_TABLE_TRAIN_BOOKINGS);
            db.execSQL(CREATE_TABLE_RAILWAYS_CUSTOMER);
            Log.d("Database", "Tables created successfully");
        } catch (Exception e) {
            Log.e("Database Error", "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS airways_passengers");
            db.execSQL("DROP TABLE IF EXISTS railways_passengers");
            db.execSQL("DROP TABLE IF EXISTS selected_train_tickets");
            db.execSQL("DROP TABLE IF EXISTS payment_details");
            db.execSQL("DROP TABLE IF EXISTS bookings");
            db.execSQL("DROP TABLE IF EXISTS train_bookings");
            db.execSQL("DROP TABLE IF EXISTS railways_customer");
            onCreate(db);

            if (oldVersion < 16) { // ✅ Check if upgrading from an older version
                db.execSQL("ALTER TABLE bookings ADD COLUMN status TEXT DEFAULT 'Booked'"); // ✅ Add status column
            }

            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='bookings'", null);
            if (cursor.getCount() > 0) {
                Log.d("Database", "✅ Bookings table exists.");
            } else {
                Log.e("Database", "❌ Bookings table does NOT exist!");
            }
            cursor.close();

        } catch (Exception e) {
            Log.e("Database Error", "Error upgrading tables: " + e.getMessage());
        }
    }

    public boolean insertsRailwaysPassengers(String trainName, String departure, String arrival, String travelDate,
                                            String classPreference, String seatNumbers, String mealPreference,
                                            List<String> passengerNames, List<Integer> passengerAges,
                                            List<String> passengerGenders, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = true;

        for (int i = 0; i < passengerNames.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("train_name", trainName);
            values.put("departure", departure);
            values.put("arrival", arrival);
            values.put("travel_date", travelDate);
            values.put("class_preference", classPreference);
            values.put("seat_numbers", seatNumbers);
            values.put("meal_preference", mealPreference);
            values.put("passenger_name", passengerNames.get(i));
            values.put("age", passengerAges.get(i));
            values.put("gender", passengerGenders.get(i));
            values.put("contact", contact);

            long result = db.insert("railways_passengers", null, values);
            if (result == -1) success = false;
        }

        db.close();
        return success;
    }




    // ✅ Insert Payment Details for Train Booking
    public boolean insertPaymentDetails(String paymentMethod, String cardNumber, String expiryDate, String cvv, String upiId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("payment_method", paymentMethod);
        values.put("card_number", cardNumber != null ? cardNumber : ""); // Handle null values
        values.put("expiry_date", expiryDate != null ? expiryDate : "");
        values.put("cvv", cvv != null ? cvv : "");
        values.put("upi_id", upiId != null ? upiId : "");

        long result = db.insert("payment_details", null, values);
        db.close();
        return result != -1;
    }
    // ✅ Insert Train Ticket into Database
    public boolean insertSelectedTrainTicket(String trainName, String departureTime, String arrivalTime, int price, int numPassengers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pnr", generatePNR()); // Generate a unique PNR number
        values.put("train_name", trainName);
        values.put("departure_time", departureTime);
        values.put("arrival_time", arrivalTime);
        values.put("price", price);
        values.put("num_passengers", numPassengers);
        values.put("status", "Booked");

        long result = db.insert("selected_train_tickets", null, values);
        db.close();
        return result != -1;
    }

    // ✅ Generate Unique PNR Number
    private String generatePNR() {
        Random random = new Random();
        return "PNR" + (100000 + random.nextInt(900000)); // Example: PNR123456
    }

    public boolean insertRailwaysCustomer(String fullName, int age, String gender, String contact, String idNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("age", age);
        values.put("gender", gender);
        values.put("contact", contact);
        values.put("id_number", idNumber);

        long result = db.insert("railways_customer", null, values);

        if (result == -1) {
            Log.e("DB_INSERT", "❌ Failed to insert customer: " + fullName);
            return false;
        } else {
            Log.d("DB_INSERT", "✅ Customer inserted successfully: " + fullName);
            return true;
        }
    }


    public Cursor getFlightTicketDetails(String bookingRef) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("Database", "Fetching ticket for BookingRef: " + bookingRef);

        // ✅ Ensure you are using the correct table name
        return db.rawQuery("SELECT * FROM bookings WHERE bookingReference=?", new String[]{bookingRef});
    }



    public boolean cancelTicket(String bookingRef) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "Cancelled"); // ✅ Update status to "Cancelled"

        // ✅ Debug: Check if the ticket exists before updating
        Cursor cursor = db.rawQuery("SELECT * FROM bookings WHERE bookingReference = ?", new String[]{bookingRef});
        if (cursor.getCount() == 0) {
            Log.e("Database", "❌ No ticket found with BookingRef: " + bookingRef);
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();

        // ✅ Update ticket status to "Cancelled"
        int rowsAffected = db.update("bookings", values, "bookingReference = ?", new String[]{bookingRef});

        if (rowsAffected > 0) {
            Log.d("Database", "✅ Ticket with BookingRef " + bookingRef + " cancelled successfully.");
        } else {
            Log.e("Database", "❌ Error: Ticket with BookingRef " + bookingRef + " NOT UPDATED!");
        }

        db.close();
        return rowsAffected > 0;
    }



    // ✅ Fetch Train Ticket Using PNR
    public Cursor getTrainTicketByPNR(String pnr) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM selected_train_tickets WHERE pnr=?", new String[]{pnr});
    }

    // ✅ Cancel Train Ticket Using PNR
    public boolean cancelTrainTicket(String pnr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "Cancelled");

        int rowsAffected = db.update("selected_train_tickets", values, "pnr=?", new String[]{pnr});
        db.close();
        return rowsAffected > 0;
    }

    // ✅ Insert Airways Customer Details
    public boolean insertAirwaysCustomer(String fullName, String passport, String dob, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("passport", passport);
        values.put("dob", dob);
        values.put("contact", contact);

        long result = db.insert("airways_customers", null, values);
        db.close();
        return result != -1; // Returns true if insertion is successful
    }

    // ✅ Check if Passport Number Already Exists
    public boolean isPassportExists(String passport) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM airways_customers WHERE passport=?", new String[]{passport});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ✅ Check if Contact Number Already Exists
    public boolean isContactExists(String contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM airways_customers WHERE contact=?", new String[]{contact});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertPassengerDetails(List<String> names, List<Integer> ages, List<String> genders, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = true;

        for (int i = 0; i < names.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("name", names.get(i));
            values.put("age", ages.get(i));
            values.put("gender", genders.get(i));
            values.put("email", email);
            values.put("phone", phone);

            long result = db.insert("passengers", null, values);
            if (result == -1) {
                success = false;
            }
        }
        db.close();
        return success;
    }

    public boolean insertBookingDetails(String bookingReference, String searchedFlight, String selectedFlight,
                                        String selectedSeats, String mealPreference, String passengerDetails,
                                        String paymentMethod, int totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("bookingReference", bookingReference);
        values.put("searchedFlight", searchedFlight);
        values.put("selectedFlight", selectedFlight);
        values.put("selectedSeats", selectedSeats);
        values.put("mealPreference", mealPreference);
        values.put("passengerDetails", passengerDetails);
        values.put("paymentMethod", paymentMethod);
        values.put("totalAmount", totalAmount);

        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
    }

    public boolean isContactsExists(String contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM railways_customer WHERE contact = ?", new String[]{contact});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Log.d("DB_CHECK", "Contact Exists: " + exists + " for " + contact);
        return exists;
    }

    public boolean isIDNumberExists(String idNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM railways_customer WHERE id_number = ?", new String[]{idNumber});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Log.d("DB_CHECK", "ID Number Exists: " + exists + " for " + idNumber);
        return exists;
    }


    public boolean isUPIExists(String upiId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM payment_details WHERE upi_id = ?", new String[]{upiId});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean isCardExists(String cardNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM payment_details WHERE card_number = ?", new String[]{cardNumber});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertTrainBooking(String trainDetails, String passengerInfo, String seatNumbers, String totalFare, String pnrNumber, String paymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("train_details", trainDetails);
        values.put("passenger_info", passengerInfo);
        values.put("seat_numbers", seatNumbers);
        values.put("total_fare", totalFare);
        values.put("pnr_number", pnrNumber);
        values.put("payment_method", paymentMethod);
        values.put("status", "Booked");  // Default status

        long result = db.insert(TABLE_TRAIN_BOOKINGS, null, values);
        db.close();

        return result != -1;  // Return true if insertion is successful
    }


}
