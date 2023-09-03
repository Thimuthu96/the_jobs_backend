package com.bscse.the_jobs.models;

public class JobSeeker extends User {
    private String job_field;
    private String gender;
    private String dob;

    public JobSeeker(String name, String nic, String email, String password, int contactNumber, String userRole) {
        super(name, nic, email, password, contactNumber, userRole);
    }

    public JobSeeker() {

    }

    public String getJob_field() {
        return job_field;
    }

    public void setJob_field(String job_field) {
        this.job_field = job_field;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}
