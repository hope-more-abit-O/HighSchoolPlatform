package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.PostCreateUniversityRequestResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.service.CreateUniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing university creation requests.
 *
 * <p> This controller provides endpoints for handling university creation requests
 * submitted by staff users. It includes functionality for creating, accepting,
 * and rejecting these requests.
 *
 * @see CreateUniversityService
 */
@RestController
@RequestMapping("/api/v1/create-university")
@RequiredArgsConstructor
public class CreateUniversityController {
    private final CreateUniversityService universityService;
    /**
     * Handles the submission of a university creation request.
     *
     * <p> This endpoint receives a request to create a new university
     * and delegates the processing to the `CreateUniversityService`.
     *
     * @param request The creation request data provided in the request body (JSON).
     * @return        A ResponseEntity containing the operation's result (success or failure details)
     *                and an appropriate HTTP status code.
     * @throws DataExistedException  If the request conflicts with existing data (e.g., duplicate names).
     *
     * @see CreateUniversityRequestRequest
     * @see ResponseData
     */
    @PostMapping
    public ResponseEntity<?> sendCreateUniversityRequest(@RequestBody @Valid CreateUniversityRequestRequest request){
        ResponseData<PostCreateUniversityRequestResponse> response = universityService.createCreateUniversityRequest(request);
        if (response.getStatus() != ResponseCode.C200.getCode())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getMessage());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptCreateUniversityRequest(@PathVariable("id") Integer id){
        return ResponseEntity.ok(universityService.adminAction(id, CreateUniversityRequestStatus.ACCEPTED));
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectCreateUniversityRequest(@PathVariable("id") Integer id){
        return ResponseEntity.ok(universityService.adminAction(id, CreateUniversityRequestStatus.REJECTED));
    }
}
