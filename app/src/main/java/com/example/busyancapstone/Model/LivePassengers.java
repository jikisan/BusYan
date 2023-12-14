package com.example.busyancapstone.Model;

public class LivePassengers {

    String passengerId;
    double lattitude;
    double longitude;
    String dateTime;

    public LivePassengers() {
    }

    public LivePassengers(String passengerId, double lattitude, double longitude, String dateTime) {
        this.passengerId = passengerId;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
