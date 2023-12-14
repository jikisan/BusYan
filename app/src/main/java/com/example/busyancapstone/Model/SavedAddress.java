package com.example.busyancapstone.Model;

public class SavedAddress {

    String userId;
    String addressName;
    Double latitude;
    Double longitude;
    String dateAdded;

    public SavedAddress() {
    }

    public SavedAddress(String userId, String addressName, Double latitude, Double longitude, String dateAdded) {
        this.userId = userId;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateAdded = dateAdded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
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

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
