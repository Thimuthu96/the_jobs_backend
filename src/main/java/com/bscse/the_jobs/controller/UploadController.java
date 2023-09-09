package com.bscse.the_jobs.controller;


import com.bscse.the_jobs.Factory.ServiceFactory;
import com.bscse.the_jobs.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class UploadController {

    private final ServiceFactory serviceFactory;




    @Autowired
    public UploadController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }






    @PostMapping("/upload-cv")
    public String uploadFile(@RequestBody MultipartFile file)throws IOException {
        UploadService uploadService = serviceFactory.createUploadService();
        return "File uploaded successfully. Download URL: " + uploadService.uploadFile(file);
    }





}
