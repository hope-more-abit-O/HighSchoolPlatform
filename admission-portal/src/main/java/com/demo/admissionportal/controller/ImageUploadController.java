package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.FirebaseStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class ImageUploadController {
    private final FirebaseStorageService firebaseStorageService;
    /**
     * Uploads multiple images to Firebase Storage.
     *
     * @param imageFile An array of MultipartFile objects representing the images to upload.
     * @return A response entity containing the URL of the uploaded images (concatenated) or a bad request message if the upload fails.
     */
    @PostMapping("/multiple")
    public ResponseEntity<String> uploadMultipleImage(@RequestParam("imageFile") MultipartFile[] imageFile) {
        String imageUrl = null;
        try {
            imageUrl = firebaseStorageService.uploadMultipleFiles(imageFile);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(imageUrl);
    }
    /**
     * Uploads a single image to Firebase Storage.
     *
     * @param imageFile A MultipartFile object representing the image to upload.
     * @return A response entity containing the URL of the uploaded image or a bad request message if the upload fails.
     */
    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("imageFile") MultipartFile imageFile) {
        String imageUrl = null;
        try {
            imageUrl = firebaseStorageService.uploadFile(imageFile);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(imageUrl);
    }
}