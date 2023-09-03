package com.bscse.the_jobs.models;

public class User {
    private String name;
    private String nic;
    private String email;
    private String password;
    private int contactNumber;
    private String userRole;

    public User() {
    }

    public User(String name, String nic, String email, String password, int contactNumber, String userRole) {
        this.name = name;
        this.nic = nic;
        this.email = email;
        this.password = password;
        this.contactNumber = contactNumber;
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}
