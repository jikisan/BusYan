package com.example.busyancapstone.Model;

import java.util.Comparator;

public class LiveBus {

    private String busDriverId;
    private String route;
    private String busCode;
    private String plateNumber;
    private String distance;
    private String travelTime;

    public LiveBus() {
    }

    public LiveBus(String busDriverId, String route, String busCode, String plateNumber, String distance, String travelTime) {
        this.busDriverId = busDriverId;
        this.route = route;
        this.busCode = busCode;
        this.plateNumber = plateNumber;
        this.distance = distance;
        this.travelTime = travelTime;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public static class DistanceComparator implements Comparator<LiveBus> {
        @Override
        public int compare(LiveBus bus1, LiveBus bus2) {
            // Extract numeric values and units
            String[] distance1Parts = bus1.getDistance().split(" ");
            String[] distance2Parts = bus2.getDistance().split(" ");

            double distance1Value = Double.parseDouble(distance1Parts[0]);
            double distance2Value = Double.parseDouble(distance2Parts[0]);

            // Convert to meters if the unit is "km"
            if (distance1Parts[1].equals("km")) {
                distance1Value *= 1000;
            }

            if (distance2Parts[1].equals("km")) {
                distance2Value *= 1000;
            }

            // Compare distances as doubles
            return Double.compare(distance1Value, distance2Value);
        }
    }
}
