package com.bscse.the_jobs.Factory;

import com.bscse.the_jobs.interfaces.SuperAdmin;
import com.bscse.the_jobs.services.*;
import org.springframework.mail.javamail.JavaMailSender;

public interface ServiceFactory {
    UserService createUserService();
    EmailService createEmailService();
    AppointmentService createAppointmentService();
    UploadService createUploadService();
    AdminServices createAdminServices();

    JavaMailSender createMailSender();
}
