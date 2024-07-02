package com.demo.admissionportal.controller.authentication;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.AuthenticationUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationUserService authenticationUserService;

    /**
     * Login response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO request) {
        if (request == null) {
            new ResponseEntity<ResponseData<LoginResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<LoginResponseDTO> loginAccount = authenticationUserService.login(request);
        if (loginAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(loginAccount);
        } else if (loginAccount.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(loginAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginAccount);
    }
}