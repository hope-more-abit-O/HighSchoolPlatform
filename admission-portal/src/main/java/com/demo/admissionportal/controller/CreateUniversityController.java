package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.PostCreateUniversityRequestResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.CreateUniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/create-university")
@RequiredArgsConstructor
public class CreateUniversityController {
    private final CreateUniversityService universityService;

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
