package com.example.busyancapstone.Model;

public class BusSchedules {
    private String bus;
    private String busDriver;
    private String conductor;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String routeNo;
    private String driverFullname;
    private String conductorFullname;
    private String datetimeAdded;
    private String busOperatorId;
    private String companyName;
    private String companyId;
    private boolean isApproved;

    public BusSchedules() {
    }

    public BusSchedules(String bus, String busDriver, String conductor, String startDate, String endDate, String startTime, String endTime, String routeNo, String driverFullname, String conductorFullname, String datetimeAdded, String busOperatorId, String companyName, String companyId, boolean isApproved) {
        this.bus = bus;
        this.busDriver = busDriver;
        this.conductor = conductor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.routeNo = routeNo;
        this.driverFullname = driverFullname;
        this.conductorFullname = conductorFullname;
        this.datetimeAdded = datetimeAdded;
        this.busOperatorId = busOperatorId;
        this.companyName = companyName;
        this.companyId = companyId;
        this.isApproved = isApproved;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public String getBus() {
        return bus;
    }

    public String getBusDriver() {
        return busDriver;
    }

    public String getConductor() {
        return conductor;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public String getDriverFullname() {
        return driverFullname;
    }

    public String getConductorFullname() {
        return conductorFullname;
    }

    public String getDatetimeAdded() {
        return datetimeAdded;
    }

    public String getBusOperatorId() {
        return busOperatorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyId() {
        return companyId;
    }
}
