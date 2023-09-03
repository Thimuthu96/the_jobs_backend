package com.bscse.the_jobs.Factory;

import com.bscse.the_jobs.services.AppointmentService;
import com.bscse.the_jobs.services.EmailService;
import com.bscse.the_jobs.services.UploadService;
import com.bscse.the_jobs.services.UserService;
import org.springframework.mail.javamail.JavaMailSender;

public interface ServiceFactory {
    UserService createUserService();
    EmailService createEmailService();
    AppointmentService createAppointmentService();
    UploadService createUploadService();

    JavaMailSender createMailSender();
}
