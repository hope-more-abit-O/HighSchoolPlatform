package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.ChangeStatusUserRequestDTO;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UpdateUserResponseDTO;
import com.demo.admissionportal.dto.response.UserProfileResponseDTO;
import com.demo.admissionportal.dto.response.UserResponseDTO;
import com.demo.admissionportal.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;

    /**
     * Gets user.
     *
     * @param username the username
     * @param email    the email
     * @return the user
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseData<List<UserResponseDTO>>> getUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        ResponseData<List<UserResponseDTO>> user = userService.getUser(username, email);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseData<UserProfileResponseDTO>> getUserById(@PathVariable("id") Integer id) {
        if (id == null || id < 0) {
            new ResponseEntity<ResponseData<UserProfileResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<UserProfileResponseDTO> user = userService.getUserById(id);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Update user response entity.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PatchMapping("/profile/{id}")
    public ResponseEntity<ResponseData<UpdateUserResponseDTO>> updateUser(@PathVariable("id") Integer id, @RequestBody @Valid UpdateUserRequestDTO requestDTO) {
        if (id == null || id < 0 || requestDTO == null) {
            new ResponseEntity<ResponseData<UpdateUserResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<UpdateUserResponseDTO> user = userService.updateUser(id, requestDTO);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    @PostMapping("/{id}/change-status/")
    public ResponseEntity<ResponseData<ChangeStatusUserRequestDTO>> changeStatus(@PathVariable("id") Integer id, @RequestBody ChangeStatusUserRequestDTO requestDTO) {
        if (id == null || id < 0 || requestDTO == null) {
            new ResponseEntity<ResponseData<ChangeStatusUserRequestDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<ChangeStatusUserRequestDTO> user = userService.changeStatus(id, requestDTO);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }
}
