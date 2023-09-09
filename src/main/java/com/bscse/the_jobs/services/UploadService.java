package com.bscse.the_jobs.services;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class UploadService {
    private final Bucket bucket;
    private Storage storage;


    @Autowired
    public UploadService() {
//        Storage storage = StorageOptions.getDefaultInstance().getService();
//        bucket = storage.get("the-jobs-d0477.appspot.com");
        bucket = null;
    }


    public String uploadFile(MultipartFile file) throws IOException {
        // Define the path in the bucket where you want to store the file
        String filePath = "uploads/" + file.getOriginalFilename();

        // Upload the file to Firebase Storage
        byte[] fileBytes = file.getBytes();
        BlobInfo blobInfo = bucket.create(filePath, fileBytes, file.getContentType());

        //Get download URL
        String downloadedUrl = getDownloadUrl(blobInfo);
        return downloadedUrl;
    }

    private String getDownloadUrl(BlobInfo blobInfo) {
        String downloadUrl = blobInfo.getMediaLink();
        return downloadUrl;
    }
}
