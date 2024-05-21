package com.example.busyancapstone.Model;

public class Job {

    private String title;
    private String location;
    private String salary;
    private String companyPhotoUrl;
    private String jobType;
    private String description;
    private String qualifications;
    private String applicationInstructions;
    private String aboutCompany;
    private String postDate;
    private String busOperatorId;
    private String companyName;
    private String companyId;
    private String preferences;

    public Job() {
    }

    public Job(String title, String location, String salary, String companyPhotoUrl, String jobType, String description, String qualifications, String applicationInstructions, String aboutCompany, String postDate, String busOperatorId, String companyName, String companyId, String preferences) {
        this.title = title;
        this.location = location;
        this.salary = salary;
        this.companyPhotoUrl = companyPhotoUrl;
        this.jobType = jobType;
        this.description = description;
        this.qualifications = qualifications;
        this.applicationInstructions = applicationInstructions;
        this.aboutCompany = aboutCompany;
        this.postDate = postDate;
        this.busOperatorId = busOperatorId;
        this.companyName = companyName;
        this.companyId = companyId;
        this.preferences = preferences;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCompanyPhotoUrl() {
        return companyPhotoUrl;
    }

    public void setCompanyPhotoUrl(String companyPhotoUrl) {
        this.companyPhotoUrl = companyPhotoUrl;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getApplicationInstructions() {
        return applicationInstructions;
    }

    public void setApplicationInstructions(String applicationInstructions) {
        this.applicationInstructions = applicationInstructions;
    }

    public String getAboutCompany() {
        return aboutCompany;
    }

    public void setAboutCompany(String aboutCompany) {
        this.aboutCompany = aboutCompany;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
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

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
