package com.example.busyancapstone.Model;

public class TempJob {

    private String Id;
    private Job job;

    public TempJob() {
    }

    public TempJob(String id, Job job) {
        Id = id;
        this.job = job;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
