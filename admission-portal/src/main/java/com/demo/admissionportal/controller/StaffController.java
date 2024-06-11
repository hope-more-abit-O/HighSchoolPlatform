package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Staff controller.
 */
@Controller
@CrossOrigin
@RequestMapping("/api/v1/staffs")
public class StaffController {
    @Autowired
    private StaffService staffService;

    /**
     * Register staff response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<?> registerStaff(@RequestBody @Valid RegisterStaffRequestDTO request) {
        return ResponseEntity.ok(staffService.registerStaff(request));
    }
}
