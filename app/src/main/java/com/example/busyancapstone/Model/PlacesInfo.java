package com.example.busyancapstone.Model;

public class PlacesInfo {

    private String place;
    private Double latitude;
    private Double longitude;

    public PlacesInfo() {
    }

    public PlacesInfo(String place, Double latitude, Double longitude) {
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
