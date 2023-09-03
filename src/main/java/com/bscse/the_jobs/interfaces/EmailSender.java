package com.bscse.the_jobs.interfaces;

import com.bscse.the_jobs.models.Email;

public interface EmailSender {
    boolean sendMail(Email email);
}
