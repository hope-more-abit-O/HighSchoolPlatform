package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.request.RegisterIdentificationNumberRequest;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UpdateUserResponseDTO;
import com.demo.admissionportal.dto.response.UserProfileResponseDTO;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;
    private final UniversityService universityService;

    /**
     * Gets user by id.
     *
     * @return the user by id
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<UserProfileResponseDTO>> getUserById() {
        ResponseData<UserProfileResponseDTO> user = userService.getUserById();
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
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<UpdateUserResponseDTO>> updateUser(@RequestBody @Valid UpdateUserRequestDTO requestDTO) {
        if (requestDTO == null) {
            new ResponseEntity<ResponseData<UpdateUserResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<UpdateUserResponseDTO> user = userService.updateUser(requestDTO);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        } else if (user.getStatus() == ResponseCode.C209.getCode()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        } else if (user.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Find full university by id response entity.
     *
     * @param id the id
     * @return the response entity
     * @throws Exception the exception
     */
    @GetMapping("/university/{id}")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> findFullUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("Lấy thông tin trường thành công", universityService.getUniversityFullResponseById(id));
        return ResponseEntity.ok(result);
    }

    /**
     * Update identification number response entity.
     *
     * @param id             the id
     * @param request        the request
     * @param authentication the authentication
     * @return the response entity
     */
    @PostMapping("/register-identification-number/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<String>> updateIdentificationNumber(
            @PathVariable("id") Integer id,
            @RequestBody @Valid RegisterIdentificationNumberRequest request,
            Authentication authentication) {
        ResponseData<String> response = userService.registerIdentificationNumber(id, request.getIdentificationNumber(), authentication);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (response.getStatus() == ResponseCode.C205.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
