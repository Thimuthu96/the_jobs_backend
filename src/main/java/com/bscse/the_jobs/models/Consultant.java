package com.bscse.the_jobs.models;

import java.util.Map;

public class Consultant extends User{
    private String accountState;
    private String specializeJobField;

    public Consultant(String name, String nic, String email, String password, int contactNumber, String userRole) {
        super(name, nic, email, password, contactNumber, userRole);
    }

    public Consultant() {

    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getSpecializeJobField() {
        return specializeJobField;
    }

    public void setSpecializeJobField(String specializeJobField) {
        this.specializeJobField = specializeJobField;
    }
}
