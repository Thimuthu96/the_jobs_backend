package com.bscse.the_jobs.config;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


@Configuration
public class CloudStorageConfig {
    @Bean
    public Bucket bucket() throws IOException {
        // Initialize and return the Bucket bean
        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("./serviceAccountKey.json")))
                .build();
        Storage storage = storageOptions.getService();
//        Storage storage = StorageOptions.getDefaultInstance().getService();
        return storage.get("the-jobs-d0477.appspot.com");
    }
}
