package com.wendyDharmawanJBusER.model;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.util.Map;

public class Schedule {
    public Timestamp departureSchedule;
    public Map<String, Boolean> seatAvailability;

    @NonNull
    @Override
    public String toString() {
        return departureSchedule.toString();
    }
}
