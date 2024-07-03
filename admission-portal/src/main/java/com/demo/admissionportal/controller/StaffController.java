package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.RegisterStaffRequest;
import com.demo.admissionportal.service.impl.StaffServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffServiceImpl staffService;

    @PostMapping
    public ResponseEntity createStaff(@RequestBody @Valid RegisterStaffRequest request){
        return ResponseEntity.ok(staffService.createStaff(request));
    }
}
