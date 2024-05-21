package com.example.busyancapstone.Model;

public class BusRoutes {

    private String BusCode;
    private String StopName;

    public BusRoutes() {
    }

    public BusRoutes(String busCode, String stopName) {
        BusCode = busCode;
        StopName = stopName;
    }

    public String getBusCode() {
        return BusCode;
    }

    public void setBusCode(String busCode) {
        BusCode = busCode;
    }

    public String getStopName() {
        return StopName;
    }

    public void setStopName(String stopName) {
        StopName = stopName;
    }
}
