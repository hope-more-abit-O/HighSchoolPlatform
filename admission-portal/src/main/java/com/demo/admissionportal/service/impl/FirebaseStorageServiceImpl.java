package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.file.UploadFileResponse;
import com.demo.admissionportal.dto.response.file.UploadMultipleFilesResponse;
import com.demo.admissionportal.entity.firebase.FirebaseFileMetadata;
import com.demo.admissionportal.service.FirebaseStorageService;
import com.demo.admissionportal.util.impl.FirebaseUtil;
import com.google.cloud.storage.Bucket;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
    private static final String FIREBASE_STORAGE_URL = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/";
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Uploads multiple files to Firebase Storage.
     * <p>
     * This method uploads each file in the provided `images` array to a separate location in the Firebase Storage bucket named "highschoolvn-dev.appspot.com". The filenames are generated using a random UUID and the original file extension. The method returns a comma-separated string containing all uploaded filenames.
     *
     * @param files An array of MultipartFile objects representing the files to upload.
     * @return A comma-separated string containing the filenames of the uploaded files.
     * @throws IOException If an I/O error occurs during the upload process.
     */
    public ResponseData uploadMultipleFiles(MultipartFile[] files) throws IOException {
        List<String> result = new ArrayList<>();
        Bucket bucket = FirebaseUtil.getStorageClient().bucket("highschoolvn-dev.appspot.com");
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString() + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));

            // Upload each file
            bucket.create(fileName, file.getBytes(), file.getContentType());

            // Fetch download tokens for each file
            String downloadTokens = fetchDownloadTokens(fileName);

            // Append the download URL to the result
            result.add(getDownloadUrl(fileName));
        }
        return ResponseData.ok("Lưu các file thành công.", new UploadMultipleFilesResponse(result));
    }
    /**
     * Uploads a single file to Firebase Storage.
     * <p>
     * This method uploads the provided `file` to a location in the Firebase Storage bucket named "highschoolvn-dev.appspot.com". The filename is generated using a random UUID and the original file extension. The method returns the uploaded filename.
     *
     * @param file A MultipartFile object representing the file to upload.
     * @return The filename of the uploaded file.
     * @throws IOException If an I/O error occurs during the upload process.
     */
    public ResponseData uploadFile(MultipartFile file) throws IOException {
        Bucket bucket = FirebaseUtil.getStorageClient().bucket("highschoolvn-dev.appspot.com");
        String fileName = UUID.randomUUID().toString() + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        bucket.create(fileName, file.getBytes(), file.getContentType());

        String downloadTokens = fetchDownloadTokens(fileName);

        return ResponseData.ok("Lưu file thành công.", new UploadFileResponse(getDownloadUrl(fileName)));
    }


    public String getDownloadUrl(String fileName) {
        String downloadTokens = fetchDownloadTokens(fileName);
        String downloadUrl = FIREBASE_STORAGE_URL + fileName +
                "?alt=media&token=" + downloadTokens;
        return downloadUrl;
    }

    public String fetchDownloadTokens(String fileName) {
        String metadataUrl = FIREBASE_STORAGE_URL +  fileName ;

        // Send GET request to fetch metadata
        FirebaseFileMetadata metadata = restTemplate.getForObject(metadataUrl, FirebaseFileMetadata.class);

        if (metadata != null && metadata.getDownloadTokens() != null) {
            return metadata.getDownloadTokens();
        } else {
            throw new RuntimeException("Failed to fetch download tokens for file: " + fileName);
        }
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