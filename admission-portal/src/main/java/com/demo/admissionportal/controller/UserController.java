package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.ChangePasswordRequest;
import com.demo.admissionportal.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "BearerAuth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Change password response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
