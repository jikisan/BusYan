package com.example.busyancapstone.Model;

public class Bookmark {

    private String userId;
    private String jobId;
    private String bookmarkDate;

    public Bookmark() {
    }

    public Bookmark(String userId, String jobId, String bookmarkDate) {
        this.userId = userId;
        this.jobId = jobId;
        this.bookmarkDate = bookmarkDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(String bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }
}
