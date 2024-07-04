package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.service.FirebaseStorageService;
import com.demo.admissionportal.util.impl.FirebaseUtil;
import com.google.cloud.storage.Bucket;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service implementation for interacting with Firebase Storage.
 *
 * This class provides methods for uploading files to a Firebase Storage bucket.
 */
@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    /**
     * Uploads multiple files to Firebase Storage.
     *
     * This method uploads each file in the provided `images` array to a separate location in the Firebase Storage bucket named "highschoolvn-dev.appspot.com". The filenames are generated using a random UUID and the original file extension. The method returns a comma-separated string containing all uploaded filenames.
     *
     * @param images An array of MultipartFile objects representing the files to upload.
     * @return A comma-separated string containing the filenames of the uploaded files.
     * @throws IOException If an I/O error occurs during the upload process.
     */
    public String uploadMultipleFiles(MultipartFile[] images) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        List<String> response = new ArrayList<>(); // Build a response string
        Bucket bucket = FirebaseUtil.getStorageClient().bucket("highschoolvn-dev.appspot.com");
        for (MultipartFile imageFile : images) {
            String fileName = UUID.randomUUID().toString() + "." + getExtension(Objects.requireNonNull(imageFile.getOriginalFilename()));
            bucket.create(fileName, imageFile.getBytes(), imageFile.getContentType());
            responseBuilder.append(fileName).append(",");
        }
        return response.toString(); // Return combined response string
    }
    /**
     * Uploads a single file to Firebase Storage.
     *
     * This method uploads the provided `file` to a location in the Firebase Storage bucket named "highschoolvn-dev.appspot.com". The filename is generated using a random UUID and the original file extension. The method returns the uploaded filename.
     *
     * @param file A MultipartFile object representing the file to upload.
     * @return The filename of the uploaded file.
     * @throws IOException If an I/O error occurs during the upload process.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        Bucket bucket = FirebaseUtil.getStorageClient().bucket("highschoolvn-dev.appspot.com");
        String fileName = UUID.randomUUID().toString() + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        FirebaseUtil.getStorageClient().bucket("highschoolvn-dev.appspot.com").create(fileName, file.getBytes(), file.getContentType());
        return fileName.toString();
    }

    /**
     * Extracts the file extension from a filename.
     *
     * This method extracts the extension (e.g., "jpg", "png") from the provided `fileName`.
     *
     * @param fileName The filename to extract the extension from.
     * @return The extracted file extension.
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
