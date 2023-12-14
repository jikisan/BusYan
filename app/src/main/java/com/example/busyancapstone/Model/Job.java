package com.example.busyancapstone.Model;

public class Job {

    private String title;
    private String company;
    private String description;
    private String jobType;
    private String location;
    private String salary;
    private String postDate;

    public Job() {
    }

    public Job(String title, String company, String description, String jobType, String location, String salary, String postDate) {
        this.title = title;
        this.company = company;
        this.description = description;
        this.jobType = jobType;
        this.location = location;
        this.salary = salary;
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
