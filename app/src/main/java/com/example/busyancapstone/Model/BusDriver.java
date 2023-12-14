package com.example.busyancapstone.Model;

public class BusDriver {

    private String driverId;
    private String fullName;
    private String email;
    private String phoneNum;
    private String busCode;
    private String route;
    private String imageUrl;
    private String plateNumber;
    private boolean isGoogleConnected;
    private boolean isFacebookConnected;

    public BusDriver() {
    }

    public BusDriver(String driverId, String fullName, String email, String phoneNum, String busCode, String route, String imageUrl, String plateNumber, boolean isGoogleConnected, boolean isFacebookConnected) {
        this.driverId = driverId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.busCode = busCode;
        this.route = route;
        this.imageUrl = imageUrl;
        this.plateNumber = plateNumber;
        this.isGoogleConnected = isGoogleConnected;
        this.isFacebookConnected = isFacebookConnected;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public boolean isGoogleConnected() {
        return isGoogleConnected;
    }

    public void setGoogleConnected(boolean googleConnected) {
        isGoogleConnected = googleConnected;
    }

    public boolean isFacebookConnected() {
        return isFacebookConnected;
    }

    public void setFacebookConnected(boolean facebookConnected) {
        isFacebookConnected = facebookConnected;
    }
}
