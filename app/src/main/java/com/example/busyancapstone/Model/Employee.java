package com.example.busyancapstone.Model;

public class Employee {
    private String fullName;
    private String email;
    private String password;
    private String phoneNum;
    private String imageUrl;
    private String type;
    private String busOperatorId;
    private String companyName;
    private String companyId;
    private String datetimeAdded;

    // Constructors
    public Employee() {
        // Default constructor
    }

    public Employee(String fullName, String email, String password, String phoneNum, String imageUrl, String type, String busOperatorId, String companyName, String companyId, String datetimeAdded) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.imageUrl = imageUrl;
        this.type = type;
        this.busOperatorId = busOperatorId;
        this.companyName = companyName;
        this.companyId = companyId;
        this.datetimeAdded = datetimeAdded;
    }

    // Getters and setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBusOperatorId() {
        return busOperatorId;
    }

    public void setBusOperatorId(String busOperatorId) {
        this.busOperatorId = busOperatorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDatetimeAdded() {
        return datetimeAdded;
    }

    public void setDatetimeAdded(String datetimeAdded) {
        this.datetimeAdded = datetimeAdded;
    }
}

