package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.AdminService;
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
 * The type Admin controller.
 */
@Controller
@RequestMapping("/api/v1/admins")
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * Register admin response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseData<?>> registerAdmin(@RequestBody @Valid RegisterAdminRequestDTO request) {
        //if request insert null
        if (request == null || request.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage()));
        }
        ResponseData<?> result = adminService.registerAdmin(request);
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
