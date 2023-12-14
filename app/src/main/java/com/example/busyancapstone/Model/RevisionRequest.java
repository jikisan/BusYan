package com.example.busyancapstone.Model;

public class RevisionRequest {

    String busDriverId;
    String date;
    String notes;

    public RevisionRequest() {
    }

    public RevisionRequest(String busDriverId, String date, String notes) {
        this.busDriverId = busDriverId;
        this.date = date;
        this.notes = notes;
    }

    public String getBusDriverId() {
        return busDriverId;
    }

    public void setBusDriverId(String busDriverId) {
        this.busDriverId = busDriverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return notes;
    }

    public void setNote(String notes) {
        this.notes = notes;
    }
}
