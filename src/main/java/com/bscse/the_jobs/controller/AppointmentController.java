package com.bscse.the_jobs.controller;


import com.bscse.the_jobs.Factory.ServiceFactory;
import com.bscse.the_jobs.models.Appointment;
import com.bscse.the_jobs.models.ConsultantAvailability;
import com.bscse.the_jobs.models.Email;
import com.bscse.the_jobs.services.AppointmentService;
import com.bscse.the_jobs.services.EmailService;
import com.bscse.the_jobs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class AppointmentController {


//    private final UserService userService;
//    private final EmailService emailService;
//    private final AppointmentService appointmentService;

    private final ServiceFactory serviceFactory;
    private static final String MSG_BODY = "You have successfully placed your appointment!\nOne of our consultant agent will approve it as soon as possible.";
    private static final String MSG_SUBJECT = "The Jobs - Appointment Queued!";





    @Autowired
    public AppointmentController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }





    @PostMapping("/appointment")
    public ResponseEntity<String> createAppointment(@RequestBody Appointment appointment){
        //make appointment for job-seeker
        AppointmentService appointmentService = serviceFactory.createAppointmentService();
        EmailService emailService = serviceFactory.createEmailService();
        try {
            boolean appointmentSuccess = appointmentService.makeAppointment(appointment);
            if (appointmentSuccess){
                Email email = new Email();
                email.setRecipient(appointment.getEmail());
                email.setMsgBody(MSG_BODY);
                email.setSubject(MSG_SUBJECT);
                emailService.sendMail(email);
                return ResponseEntity.status(HttpStatus.CREATED).body("Appointment created successfully");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create appointment");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create appointment");
        }
    }




    @GetMapping("/appointment")
    public ResponseEntity<List<Appointment>> fetchAppointmentsBySchedule(
            @RequestParam String scheduledDate,
            @RequestParam String nic
    ){
        //-------Fetch all appointments by scheduled date and time
        AppointmentService appointmentService = serviceFactory.createAppointmentService();
        try {
            List<Appointment> appointmentList = appointmentService.getAppointmentByConsultant(scheduledDate, nic);
            return ResponseEntity.ok(appointmentList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    @PutMapping("/appointment/status-update")
    public ResponseEntity<String> updateAppointmentStatus(
            @RequestParam String appointmentId,
            @RequestParam String consultantId
    )throws ExecutionException, InterruptedException{
        //-------Update appointment state
        AppointmentService appointmentService = serviceFactory.createAppointmentService();
        try {
            boolean success = appointmentService.approveAppointment(appointmentId, consultantId);
            if (success){
                return ResponseEntity.status(HttpStatus.OK).body("Appointment updated successfully!");
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Appointment couldn't update");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update appointment");
        }
    }





    @PutMapping("/appointment/complete")
    public ResponseEntity<String> appointmentComplete(
            @RequestParam String appointmentId
    )throws ExecutionException, InterruptedException{
        //-------Update appointment state
        AppointmentService appointmentService = serviceFactory.createAppointmentService();
        try {
            boolean success = appointmentService.appointmentCompleted(appointmentId);
            if (success){
                return ResponseEntity.status(HttpStatus.OK).body("Appointment completed successfully!");
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Appointment couldn't update");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update appointment");
        }
    }







    @GetMapping("/appointment/filtering")
    public ResponseEntity<List<Appointment>> filteringAppointmentData(
            @RequestParam String selectedDate,
            @RequestParam String jobCategory
    ){
        //-------Fetch all appointments by scheduled date and time
        AppointmentService appointmentService = serviceFactory.createAppointmentService();
        try {
            List<Appointment> appointmentList = appointmentService.filteringAppointmentData(selectedDate, jobCategory);
            return ResponseEntity.ok(appointmentList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





}
