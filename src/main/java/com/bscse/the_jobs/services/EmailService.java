package com.bscse.the_jobs.services;


import com.bscse.the_jobs.interfaces.EmailSender;
import com.bscse.the_jobs.models.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService implements EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendMail(Email email) {
        try {
            System.out.println("++++++++****************");
            System.out.println(email.getRecipient());
            System.out.println(email.getSubject());
            System.out.println(email.getMsgBody());
            System.out.println(email);
            System.out.println("++++++++****************");
            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(email.getRecipient());
            mailMessage.setText(email.getMsgBody());
            mailMessage.setSubject(email.getSubject());

            System.out.println("++++++++****************+++++++++");
            System.out.println(mailMessage.getFrom());
            System.out.println(mailMessage.getSubject());
            System.out.println(mailMessage.getText());
            System.out.println(mailMessage);
            System.out.println("++++++++****************+++++++++");

            // Sending the mail
            javaMailSender.send(mailMessage);
            return true;
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
