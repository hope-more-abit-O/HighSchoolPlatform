package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UserResponseDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseData<UserResponseDTO>> getUser(@PathVariable Integer id) {
        if (id == null || id < 0) {
            new ResponseEntity<ResponseData<UserResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<UserResponseDTO> user = userService.getUser(id);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }
}
