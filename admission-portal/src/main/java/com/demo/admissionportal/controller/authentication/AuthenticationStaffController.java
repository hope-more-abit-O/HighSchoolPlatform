package com.demo.admissionportal.controller.authentication;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.AuthenticationStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/staffs")
@RequiredArgsConstructor
public class AuthenticationStaffController {
    private final AuthenticationStaffService authenticationStaffService;


    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO request) {
        if (request == null) {
            new ResponseEntity<ResponseData<LoginResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<LoginResponseDTO> loginAccount = authenticationStaffService.login(request);
        if (loginAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(loginAccount);
        } else if (loginAccount.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(loginAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginAccount);
    }
}
