package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.request.ChangeStatusUserRequestDTO;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.StaffService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * The type Staff controller.
 */
@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "BearerAuth")
public class StaffController {
    private final StaffService staffService;
    private final UserService userService;
    private final CreateUniversityService createUniversityService;
    private final UniversityService universityService;

    /**
     * Handles the submission of a university creation request.
     *
     * <p> This endpoint receives a request to create a new university
     * and delegates the processing to the `CreateUniversityService`.
     *
     * @param request The creation request data provided in the request body (JSON).
     * @return        A ResponseEntity containing the operation's result (success or failure details)
     *                and an appropriate HTTP status code.
     * @throws DataExistedException  If the request conflicts with existing data (e.g., duplicate names).
     *
     * @see CreateUniversityRequestRequest
     * @see ResponseData
     */
    @PostMapping("/create-university")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<?> sendCreateUniversityRequest(@RequestBody @Valid CreateUniversityRequestRequest request){
        ResponseData<PostCreateUniversityRequestResponse> response = createUniversityService.createCreateUniversityRequest(request);
        if (response.getStatus() != ResponseCode.C200.getCode())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getMessage());
        return ResponseEntity.ok(response);
    }

    /**
     * Gets staff by id.
     *
     * @param id the id
     * @return the staff by id
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<?> getStaffById(@PathVariable int id) {
        ResponseData<?> result = staffService.getStaffById(id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff Get by ID successfully: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found by ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to Get staff by ID: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Update staff response entity.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<StaffResponseDTO>> updateStaff(@RequestBody @Valid UpdateStaffRequestDTO request, @PathVariable Integer id) {
        log.info("Received request to update staff: {}", request);
        ResponseData<StaffResponseDTO> result = staffService.updateStaff(request, id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff updated successfully with ID: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to update staff: {}", request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @param email    the email
     * @param pageable the pageable
     * @return the user
     */
    @GetMapping("/list/users")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<UserResponseDTO>>> getUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            Pageable pageable
    ) {
        ResponseData<Page<UserResponseDTO>> user = userService.getUser(username, email, pageable);
        if (user.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else if (user.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
    }

    /**
     * Change status response entity.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping("/user/{id}/change-status")
    @PreAuthorize("hasAuthority('STAFF')")
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

    @GetMapping("/university/{id}")
    public ResponseEntity<UniversityFullResponseDTO> getUniversityInfoById(@PathVariable Integer id) {
        return ResponseEntity.ok(universityService.getUniversityFullResponseById(id));
    }
}
