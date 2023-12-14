package com.example.busyancapstone.Model;

import com.example.busyancapstone.Enum.ReservationStatus;

public class SeatReservation {

    private static SeatReservation instance;

    private String busDriverId;
    private String passengerId;
    private String dateCreated;
    private String busCode;
    private String route;
    private String plateNumber;
    private String startLocation;
    private Double startLat;
    private Double startLong;
    private String destination;
    private Double destinationLat;
    private Double destinationLong;
    private ReservationStatus status;

    private SeatReservation() {
    }

    private SeatReservation(String busDriverId, String passengerId, String dateCreated, String busCode,
                           String route, String plateNumber, String startLocation, Double startLat, Double startLong, String destination, Double destinationLat, Double destinationLong, ReservationStatus status) {
        this.busDriverId = busDriverId;
        this.passengerId = passengerId;
        this.dateCreated = dateCreated;
        this.busCode = busCode;
        this.route = route;
        this.plateNumber = plateNumber;
        this.startLocation = startLocation;
        this.startLat = startLat;
        this.startLong = startLong;
        this.destination = destination;
        this.destinationLat = destinationLat;
        this.destinationLong = destinationLong;
        this.status = status;
    }

    public static SeatReservation getInstance(){
        if(instance == null) {
            instance = new SeatReservation();
            return instance;
        }

        return instance;
    }

    public String getBusDriverId() {
        return busDriverId;
    }

    public void setBusDriverId(String busDriverId) {
        this.busDriverId = busDriverId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getStartLong() {
        return startLong;
    }

    public void setStartLong(Double startLong) {
        this.startLong = startLong;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(Double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public Double getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(Double destinationLong) {
        this.destinationLong = destinationLong;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void clear(){

        setBusDriverId("");
        setPassengerId("");
        setDateCreated("");
        setBusCode("");
        setRoute("");
        setPlateNumber("");
        setStartLocation("");
        setStartLat(0.0);
        setStartLong(0.0);
        setDestination("");
        setDestinationLat(0.0);
        setDestinationLong(0.0);
        setStatus(null);
    }
}
