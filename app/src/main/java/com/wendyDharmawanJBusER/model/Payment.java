package com.wendyDharmawanJBusER.model;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Payment extends Invoice {
    private int busId;
    public Timestamp departureDate;
    public List<String> busSeats;

    @NonNull
    @Override
    public String toString() {
    return (""+departureDate);
    }
    public int getBusId(){
        return busId;
    }
}



