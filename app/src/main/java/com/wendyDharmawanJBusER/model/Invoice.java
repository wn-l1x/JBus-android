package com.wendyDharmawanJBusER.model;


import java.sql.Timestamp;

public class Invoice extends Serializable{
    public Timestamp time;
    public int buyerId;
    public BusRating rating;
    public PaymentStatus status;
    public int renterId;
    public enum BusRating
    {
        NONE, NEUTRAL, GOOD, BAD
    }
    public enum PaymentStatus
    {
        FAILED, WAITING, SUCCESS
    }
}
