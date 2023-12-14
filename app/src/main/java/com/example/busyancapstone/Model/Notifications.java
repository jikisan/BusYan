package com.example.busyancapstone.Model;

import com.example.busyancapstone.Enum.NotificationTypes;

public class Notifications {

    private String targetUserId ;
    private String relatedNodeId ;
    private String title;
    private String message;
    private String dateCreated;
    private NotificationTypes notifType;

    public Notifications() {
    }

    public Notifications(String targetUserId, String relatedNodeId, String title, String message, String dateCreated, NotificationTypes notifType) {
        this.targetUserId = targetUserId;
        this.relatedNodeId = relatedNodeId;
        this.title = title;
        this.message = message;
        this.dateCreated = dateCreated;
        this.notifType = notifType;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getRelatedNodeId() {
        return relatedNodeId;
    }

    public void setRelatedNodeId(String relatedNodeId) {
        this.relatedNodeId = relatedNodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public NotificationTypes getNotifType() {
        return notifType;
    }

    public void setNotifType(NotificationTypes notifType) {
        this.notifType = notifType;
    }
}
