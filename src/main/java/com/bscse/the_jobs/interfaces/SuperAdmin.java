package com.bscse.the_jobs.interfaces;

import com.bscse.the_jobs.models.Consultant;

import java.util.concurrent.ExecutionException;

public interface SuperAdmin {
    boolean checkAuth(String email, String password) throws ExecutionException, InterruptedException;
    boolean approveConsultant(String email) throws ExecutionException, InterruptedException;
}
