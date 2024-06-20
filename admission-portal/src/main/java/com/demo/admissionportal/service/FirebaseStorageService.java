package com.demo.admissionportal.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service interface for interacting with Firebase Storage.
 *
 * This interface defines contracts for methods that upload files to Firebase Storage. Implementations of this interface will provide the specific logic for uploading files.
 */
public interface FirebaseStorageService {

    /**
     * Uploads multiple files to Firebase Storage.
     *
     * This method takes an array of `MultipartFile` objects representing the files to upload. The implementation should upload each file to a separate location in the Firebase Storage bucket. The specific behavior (e.g., filename generation, error handling) will depend on the implementation.
     *
     * @param images An array of MultipartFile objects representing the files to upload.
     * @return A String containing information about the uploaded files. The format of the returned String depends on the implementation (e.g., comma-separated filenames, a list of URLs).
     * @throws IOException If an I/O error occurs during the upload process.
     */
    public String uploadMultipleFiles(MultipartFile[] images) throws IOException;

    /**
     * Uploads a single file to Firebase Storage.
     *
     * This method takes a single `MultipartFile` object representing the file to upload. The implementation should upload the file to a location in the Firebase Storage bucket. The specific behavior (e.g., filename generation, error handling) will depend on the implementation.
     *
     * @param file A MultipartFile object representing the file to upload.
     * @return A String containing the information about the uploaded file. The format of the returned String depends on the implementation (e.g., filename, URL).
     * @throws IOException If an I/O error occurs during the upload process.
     */
    public String uploadFile(MultipartFile file) throws IOException;
}