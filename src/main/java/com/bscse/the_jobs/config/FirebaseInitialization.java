package com.bscse.the_jobs.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseInitialization {
    private static FirebaseApp firebaseApp;
    private FirebaseInitialization() {} // Private constructor to prevent instantiation
    @PostConstruct
    public static FirebaseApp getFirebaseApp() {
        if (firebaseApp == null) {
            try {
                FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                firebaseApp = FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return firebaseApp;
    }
}
