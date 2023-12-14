package com.example.busyancapstone.Model;

public class TempNotifBusDriver {

    private String passengerName;
    private String dateCreated;
    private String seatReservationId;

    public TempNotifBusDriver() {
    }

    public TempNotifBusDriver(String passengerName, String dateCreated, String seatReservationId) {
        this.passengerName = passengerName;
        this.dateCreated = dateCreated;
        this.seatReservationId = seatReservationId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getSeatReservationId() {
        return seatReservationId;
    }

    public void setSeatReservationId(String seatReservationId) {
        this.seatReservationId = seatReservationId;
    }
}

