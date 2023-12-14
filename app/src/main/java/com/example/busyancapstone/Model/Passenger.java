package com.example.busyancapstone.Model;

public class Passenger {

    private String passengerId;
    private String fullName;
    private String email;
    private String phoneNum;
    private String imageUrl;
    private boolean isGoogleConnected;

    public Passenger() {
    }

    public Passenger(String passengerId, String fullName, String email, String phoneNum, String imageUrl, boolean isGoogleConnected) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.imageUrl = imageUrl;
        this.isGoogleConnected = isGoogleConnected;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isGoogleConnected() {
        return isGoogleConnected;
    }

    public void setGoogleConnected(boolean googleConnected) {
        isGoogleConnected = googleConnected;
    }
}
