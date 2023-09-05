package com.bscse.the_jobs.models;

import java.time.LocalDateTime;
import java.util.Map;

public class Appointment extends JobSeeker {
    private String appointmentId;
    private String consultantId;
    private String cvUrl;
    private String scheduledDate;
    private Map<String, Boolean> scheduledTime;
    private String appointmentState;

    public Appointment(){
    }

    public Appointment(String name, String nic, String email, String password, int contactNumber, String userRole, String appointmentId, String consultantId, String cvUrl, String scheduledDate, Map<String, Boolean> scheduledTime, String appointmentState) {
        super(name, nic, email, password, contactNumber, userRole);
        this.appointmentId = appointmentId;
        this.consultantId = consultantId;
        this.cvUrl = cvUrl;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.appointmentState = appointmentState;
    }

    public String getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    public String getCvUrl() {
        return cvUrl;
    }
    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }
    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Map<String, Boolean> getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Map<String, Boolean> scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    public String getAppointmentState() {
        return appointmentState;
    }
    public void setAppointmentState(String appointmentState) {
        this.appointmentState = appointmentState;
    }
}
