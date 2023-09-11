package com.bscse.the_jobs.controller;

import com.bscse.the_jobs.Factory.ServiceFactory;
import com.bscse.the_jobs.models.*;
import com.bscse.the_jobs.services.AppointmentService;
import com.bscse.the_jobs.services.EmailService;
import com.bscse.the_jobs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/api/v1")
public class UserController {


//    private final UserService userService;
//    private final EmailService emailService;
//    private final AppointmentService appointmentService;

    private final ServiceFactory serviceFactory;

    private static final String MSG_BODY = "You have successfully registered with The Jobs consultation service!";
    private static final String MSG_SUBJECT = "The Jobs - Registration Completed!";




    @Autowired
    public UserController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }





    @PostMapping("/jobseeker/add")
    public ResponseEntity<String> addJobseekerData(@RequestBody JobSeeker jobSeeker){
        //Add job-seeker data and create user account
        UserService userService = serviceFactory.createUserService();
        try {
            boolean checkAccount = userService.checkIfRegistered(jobSeeker.getNic());
            if (checkAccount == false){
                userService.addJobseekerData(jobSeeker);
                boolean signupSuccess = userService.signup(jobSeeker.getEmail(), jobSeeker.getPassword());
                if (signupSuccess){
                    EmailService emailService = serviceFactory.createEmailService();
                    Email email = new Email();
                    email.setRecipient(jobSeeker.getEmail());
                    email.setMsgBody(MSG_BODY);
                    email.setSubject(MSG_SUBJECT);
                    System.out.println("****************");
                    System.out.println(email.getRecipient());
                    System.out.println(email.getSubject());
                    System.out.println(email.getMsgBody());
                    System.out.println(email);
                    System.out.println("****************");

                    boolean isEmailSent = emailService.sendMail(email);
                    if (isEmailSent){
                        return ResponseEntity.status(HttpStatus.OK).body("User created and email sent successfully for your mail!");
                    }else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to sent email.");
                    }

                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This nic already have an account! ");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }





    @GetMapping("/jobseeker/check/appointment")
    public ResponseEntity<List<Appointment>> checkJobSeekerAppointmentsState(@RequestParam String nic){
        UserService userService = serviceFactory.createUserService();
        AppointmentService appointmentService = serviceFactory.createAppointmentService();
        try{
            boolean checkAccount = userService.checkIfRegistered(nic);
            if (checkAccount == true){
                List<Appointment> appointmentList = appointmentService.fetchAppointmentByUser(nic);
                return ResponseEntity.ok(appointmentList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    @PostMapping("/consultant/add")
    public ResponseEntity<String> addConsultantData(@RequestBody Consultant consultant){
        //Add consultant data and create user account
        UserService userService = serviceFactory.createUserService();
        try {
            userService.addConsultantData(consultant);
            boolean signupSuccess = userService.signup(consultant.getEmail(), consultant.getPassword());
            if (signupSuccess){
                Email email = new Email();
                EmailService emailService = serviceFactory.createEmailService();
                email.setRecipient(consultant.getEmail());
                email.setMsgBody(MSG_BODY);
                email.setSubject(MSG_SUBJECT);

                boolean isEmailSent = emailService.sendMail(email);
                if (isEmailSent){
                    return ResponseEntity.status(HttpStatus.OK).body("User created and email sent successfully");
                }else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user.");
                }


            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }





    @PostMapping("/consultant/availability")
    public ResponseEntity<String> addConsultantAvailability(@RequestBody ConsultantAvailability consultantAvailability){
        //Make consultant availability
        UserService userService = serviceFactory.createUserService();
        try {
            boolean isValidUserMail = userService.checkConsultantUser(consultantAvailability.getEmail());
            if (isValidUserMail){userService.addConsultantAvailability(consultantAvailability);
                return ResponseEntity.status(HttpStatus.OK).body("Successfully added your availability.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body("Invalid user.");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add availability.");
        }
    }





    @GetMapping("/consultant/check/appointment")
    public ResponseEntity<List<Appointment>> getAppointByConsultant(@RequestParam String consultantId){
        UserService userService = serviceFactory.createUserService();
        AppointmentService appointmentService = serviceFactory.createAppointmentService();
        try{
//            boolean checkAccount = userService.checkIfRegistered(nic);
//            if (checkAccount == true){
            List<Appointment> appointmentList = appointmentService.fetchAppointmentByConsultantId(consultantId);
            return ResponseEntity.ok(appointmentList);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    @GetMapping("/validate-user-role")
    public ResponseEntity<String> filterUsersByEmailAndJobRole(
            @RequestParam String email,
            @RequestParam String userRole
    ) {
        //Check validity of user role for given email
        UserService userService = serviceFactory.createUserService();
        try {
            switch (userRole){
                case "jobseeker":
                    boolean validateResponse = userService.checkJobSeekerUser(email);
                    if (validateResponse){
                        return ResponseEntity.status(HttpStatus.OK).body("Email validated!");
                    }else{
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have sufficient access rights!");
                    }
                case "consultant":
                    boolean validateResponse1 = userService.checkConsultantUser(email);
                    if (validateResponse1){
                        return ResponseEntity.status(HttpStatus.OK).body("Email validated!");
                    }else{
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have sufficient access rights!");
                    }
                default:
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have sufficient access rights!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    @PutMapping("/consultant/status-update")
    public ResponseEntity<String> updateConsultantStatus(
            @RequestBody String nic
    )throws ExecutionException, InterruptedException{
        //-------Update consultant state
        try {
            UserService userService = serviceFactory.createUserService();
            boolean success = userService.updateConsultantState(nic);
            if (success){
                return ResponseEntity.status(HttpStatus.OK).body("Consultant status updated successfully!");
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Consultant couldn't update");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update consultant");
        }
    }




    @GetMapping("/consultants-info")
    public ResponseEntity<List<Consultant>> fetchConsultantByNic(
            @RequestParam String nic
    ){
        //-------Fetch consultant data by email
        UserService userService = serviceFactory.createUserService();
        try {
            List<Consultant> consultantDetailList = userService.getConsultantData(nic);
            return ResponseEntity.ok(consultantDetailList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    @GetMapping("/consultant-nic")
    public ResponseEntity<String> fetchConsultantByEmail(
            @RequestParam String email
    ){
        //-------Fetch consultant data by email
        UserService userService = serviceFactory.createUserService();
        try {
            String consultantNic = userService.getConsultantDataByEmail(email);
            return ResponseEntity.ok(consultantNic);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    @PutMapping("/consultant/update")
    public ResponseEntity<String> updateConsultantData(
            @RequestBody Consultant consultant
    )throws ExecutionException, InterruptedException{
        //-------Update consultant data
        try {
            UserService userService = serviceFactory.createUserService();
            boolean success = userService.updateConsultantData(consultant);
            if (success){
                return ResponseEntity.status(HttpStatus.OK).body("Consultant data updated successfully!");
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Consultant couldn't update");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update consultant");
        }
    }





    @GetMapping("/consultant-availability-data")
    public ResponseEntity<List<ConsultantAvailability>> getAvailabilityDataByUser(
            @RequestParam String nic
    ){
        //-------Fetch consultant availability data by email
        UserService userService = serviceFactory.createUserService();
        try{
            List<ConsultantAvailability> consultantAvailabilities = userService.getAvailabilityDataByUser(nic);
            return ResponseEntity.ok(consultantAvailabilities);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
