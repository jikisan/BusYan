package com.example.busyancapstone.Model;

public class LiveDrivers {

    private String busDriverId;
    private String route;
    private String busCode;
    private String plateNumber;
    private double lattitude;
    private double longitude;
    private String dateTime;

    public LiveDrivers() {
    }


    public LiveDrivers(String busDriverId, String route, String busCode, String plateNumber, double lattitude, double longitude, String dateTime) {
        this.busDriverId = busDriverId;
        this.route = route;
        this.busCode = busCode;
        this.plateNumber = plateNumber;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
    }

    public String getBusDriverId() {
        return busDriverId;
    }

    public void setBusDriverId(String busDriverId) {
        this.busDriverId = busDriverId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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
