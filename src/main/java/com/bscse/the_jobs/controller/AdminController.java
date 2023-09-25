package com.bscse.the_jobs.controller;


import com.bscse.the_jobs.Factory.ServiceFactory;
import com.bscse.the_jobs.services.AdminServices;
import com.bscse.the_jobs.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class AdminController {
    private final ServiceFactory serviceFactory;

    @Autowired
    public AdminController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @GetMapping("/superadmin/auth")
    public boolean fetchAppointmentsBySchedule(
            @RequestParam String email,
            @RequestParam String password
    ){
        //-------Fetch all appointments by scheduled date and time
        AdminServices adminServices = serviceFactory.createAdminServices();
        try {
            boolean checkAdminAuth = adminServices.checkAuth(email, password);
            if (checkAdminAuth == true){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }





    @PutMapping("/superadmi/approvals")
    public ResponseEntity<String> updateAppointmentStatus(
            @RequestParam String email
    )throws ExecutionException, InterruptedException{
        //-------Update consultant state
        AdminServices adminServices = serviceFactory.createAdminServices();
        try {
            boolean success = adminServices.approveConsultant(email);
            if (success == true){
                return ResponseEntity.status(HttpStatus.OK).body("Consultant account approval successfully!");
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Consultant couldn't update");
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update consultant account");
        }
    }
}
