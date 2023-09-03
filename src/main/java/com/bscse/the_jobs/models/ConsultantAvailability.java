package com.bscse.the_jobs.models;

import java.util.Map;

public class ConsultantAvailability extends Consultant{

    private String date;
    private Map<String, Boolean> availableTime;

    public ConsultantAvailability(){}

    public ConsultantAvailability(String name, String nic, String email, String password, int contactNumber, String userRole) {
        super(name, nic, email, password, contactNumber, userRole);
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Boolean> getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(Map<String, Boolean> availableTime) {
        this.availableTime = availableTime;
    }
}
