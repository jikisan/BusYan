package com.example.busyancapstone.Model;

public class BusDetails {

    private Long idNo;
    private String busCode;
    private String startPoint;
    private String endPoint;
    private String plateNumber;
    private String datetimeAdded;
    private String companyName;
    private String companyId;
    private String busOperatorId;


    public BusDetails() {
    }

    public BusDetails(Long idNo, String busCode, String startPoint, String endPoint, String plateNumber, String datetimeAdded, String companyName, String companyId, String busOperatorId) {
        this.idNo = idNo;
        this.busCode = busCode;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.plateNumber = plateNumber;
        this.datetimeAdded = datetimeAdded;
        this.companyName = companyName;
        this.companyId = companyId;
        this.busOperatorId = busOperatorId;
    }

    public Long getIdNo() {
        return idNo;
    }

    public void setIdNo(Long idNo) {
        this.idNo = idNo;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDatetimeAdded() {
        return datetimeAdded;
    }

    public void setDatetimeAdded(String datetimeAdded) {
        this.datetimeAdded = datetimeAdded;
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

    public String getBusOperatorId() {
        return busOperatorId;
    }

    public void setBusOperatorId(String busOperatorId) {
        this.busOperatorId = busOperatorId;
    }
}
