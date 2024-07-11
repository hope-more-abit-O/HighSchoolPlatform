package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.FirebaseStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileUploadController {
    private final FirebaseStorageService firebaseStorageService;
    /**
     * Uploads multiple images to Firebase Storage.
     *
     * @param imageFile An array of MultipartFile objects representing the images to upload.
     * @return A response entity containing the URL of the uploaded images (concatenated) or a bad request message if the upload fails.
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity uploadMultipleFile(
            @RequestBody(content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(type = "array", format = "binary")))
            @RequestParam("imageFile") MultipartFile[] imageFile) {
        List<String> result = null;
        try {
            return ResponseEntity.ok(firebaseStorageService.uploadMultipleFiles(imageFile));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /**
     * Uploads a single image to Firebase Storage.
     *
     * @param imageFile A MultipartFile object representing the image to upload.
     * @return A response entity containing the URL of the uploaded image or a bad request message if the upload fails.
     */
    @RequestMapping(
            path = "/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFile(@RequestParam("imageFile") MultipartFile imageFile) {
        String imageUrl = null;
        try {
            return  ResponseEntity.ok(firebaseStorageService.uploadFile(imageFile));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}