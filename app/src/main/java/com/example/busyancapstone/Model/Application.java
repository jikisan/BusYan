package com.example.busyancapstone.Model;

public class Application {

    private String applicantId;
    private String jobId;
    private String profileUrl;
    private String question1;
    private String question2;
    private String resumeUrl;
    private String workExperience;
    private String educationalAttainment;
    private String address;
    private double longitude;
    private double latitude;
    private String licenseUrl;
    private String additionalInfo;
    private String applicationDateCreated;

    public Application() {
    }

    public Application(String applicantId, String jobId, String profileUrl, String question1, String question2, String resumeUrl, String workExperience, String educationalAttainment, String address, double longitude, double latitude, String licenseUrl, String additionalInfo, String applicationDateCreated) {
        this.applicantId = applicantId;
        this.jobId = jobId;
        this.profileUrl = profileUrl;
        this.question1 = question1;
        this.question2 = question2;
        this.resumeUrl = resumeUrl;
        this.workExperience = workExperience;
        this.educationalAttainment = educationalAttainment;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.licenseUrl = licenseUrl;
        this.additionalInfo = additionalInfo;
        this.applicationDateCreated = applicationDateCreated;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getEducationalAttainment() {
        return educationalAttainment;
    }

    public void setEducationalAttainment(String educationalAttainment) {
        this.educationalAttainment = educationalAttainment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getApplicationDateCreated() {
        return applicationDateCreated;
    }

    public void setApplicationDateCreated(String applicationDateCreated) {
        this.applicationDateCreated = applicationDateCreated;
    }
}
