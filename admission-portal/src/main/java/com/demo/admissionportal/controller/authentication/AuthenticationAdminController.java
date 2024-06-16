package com.demo.admissionportal.controller.authentication;

import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.AuthenticationAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Authentication admin controller.
 */
@RestController
@RequestMapping("/api/v1/auth/admins")
@RequiredArgsConstructor
public class AuthenticationAdminController {
    private final AuthenticationAdminService authenticationAdminService;

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
        ResponseData<LoginResponseDTO> loginAccount = authenticationAdminService.login(request);
        if (loginAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount);
        } else {
            return ResponseEntity.status(loginAccount.getStatus()).body(loginAccount);
        }
    }
}
