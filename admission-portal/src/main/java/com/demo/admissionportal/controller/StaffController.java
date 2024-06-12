package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/register")
    public ResponseEntity<ResponseData<?>> registerStaff(@RequestBody @Valid RegisterStaffRequestDTO request) {
        //if request insert null
        if (request == null || request.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage()));
        }
        ResponseData<?> result = staffService.registerStaff(request);
        //if request full field
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
            //if duplicate field
        } else if (result.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        //others
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
