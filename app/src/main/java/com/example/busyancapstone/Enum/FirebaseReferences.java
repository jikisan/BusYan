package com.example.busyancapstone.Enum;

public enum FirebaseReferences {

    PASSENGER("Passengers"),
    BUS_DRIVER("BusDrivers"),
    BUS_ROUTES("BusRoutes"),
    EMPLOYEES("Employees"),
    RESERVATIONS("SeatReservations"),
    JOBS("Jobs"),
    APPLICATIONS("Applications"),
    LIVE_DRIVERS("LiveDrivers"),
    LIVE_PASSENGERS("LivePassengers"),
    REVISION_REQUEST("RevisionRequest"),
    NOTIFICATIONS("Notifications"),
    BOOKMARK_ADDRESS("BookmarkAddress"),
    BOOKMARK_JOBS("BookmarkJobs");

    private String databaseName;

    private FirebaseReferences(String databaseName){
        this.databaseName = databaseName;
    }

    public String getValue(){
        return databaseName;
    }
}
