package com.demo.admissionportal.util.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseUtil {

    @Getter
    private static StorageClient storageClient;

    static {
        initializeFirebase();
    }

    private static void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-adminsdk.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
                storageClient = StorageClient.getInstance();
            } else {
                storageClient = StorageClient.getInstance(FirebaseApp.getInstance());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
