package com.bscse.the_jobs.interfaces;

public interface AuthService {
    boolean signup(String email, String password);
    boolean checkJobSeekerUser(String email);
    boolean checkConsultantUser(String email);

    boolean checkIfRegistered(String email);
}
