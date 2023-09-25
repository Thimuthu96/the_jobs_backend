package com.bscse.the_jobs.Factory;

import com.bscse.the_jobs.interfaces.SuperAdmin;
import com.bscse.the_jobs.services.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.InputStream;


@Component
public class ServiceFactoryImplementation implements ServiceFactory{

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public ServiceFactoryImplementation(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public UserService createUserService() {
        return new UserService();
    }

    @Override
    public EmailService createEmailService() {
        return new EmailService(javaMailSender);
    }

    @Override
    public AppointmentService createAppointmentService() {
        return new AppointmentService();
    }

    @Override
    public UploadService createUploadService() {
        return new UploadService();
    }

    @Override
    public JavaMailSender createMailSender() {
        return new JavaMailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {

            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {

            }

            @Override
            public MimeMessage createMimeMessage() {
                return null;
            }

            @Override
            public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
                return null;
            }

            @Override
            public void send(MimeMessage mimeMessage) throws MailException {

            }

            @Override
            public void send(MimeMessage... mimeMessages) throws MailException {

            }

            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

            }

            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

            }
        };
    }

    @Override
    public AdminServices createAdminServices() { return new AdminServices();}
}
