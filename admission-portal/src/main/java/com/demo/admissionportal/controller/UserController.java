package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UpdateUserResponseDTO;
import com.demo.admissionportal.dto.response.UserProfileResponseDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.impl.UniversityServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * @param id the id
     * @return the user by id
     */
    @GetMapping("/profile/{id}")
    @PreAuthorize("hasAuthority('USER')")
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
    @PutMapping("/profile/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<UpdateUserResponseDTO>> updateUser(@PathVariable("id") Integer id, @RequestBody @Valid UpdateUserRequestDTO requestDTO) {
        if (id == null || id < 0 || requestDTO == null) {
            new ResponseEntity<ResponseData<UpdateUserResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<UpdateUserResponseDTO> user = userService.updateUser(id, requestDTO);
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

    @GetMapping("/university/{id}")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> findFullUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("Lấy thông tin trường thành công",universityService.getUniversityFullResponseById(id));
        return ResponseEntity.ok(result);
    }

}
