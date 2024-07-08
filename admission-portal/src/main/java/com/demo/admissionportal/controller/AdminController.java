package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.*;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestAdminActionRequest;
import com.demo.admissionportal.dto.request.university.DeleteUniversityRequest;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.StaffResponseDTO;
import com.demo.admissionportal.entity.AdminInfo;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.service.AdminService;
import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.StaffService;
import com.demo.admissionportal.service.UniversityService;
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

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final StaffService staffService;
    private final CreateUniversityService createUniversityService;
    private final UniversityService universityService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData<AdminInfo>> registerAdmin(@RequestBody @Valid RegisterAdminRequestDTO request) {
        ResponseData<AdminInfo> response = adminService.registerAdmin(request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/register-staff")
    public ResponseEntity<ResponseData<RegisterStaffResponse>> registerStaff(@RequestBody @Valid RegisterStaffRequestDTO request) {
        ResponseData<RegisterStaffResponse> response = staffService.registerStaff(request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/list-all-staffs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<Page<StaffResponseDTO>>> findAllStaff(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) AccountStatus status,
            Pageable pageable) {
        ResponseData<Page<StaffResponseDTO>> response = staffService.findAll(username, firstName,middleName, lastName, email, phone, status, pageable);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/get-staff/{id}")
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

    @DeleteMapping("/delete-staff/{id}")
    public ResponseEntity<?> deleteStaffById(@Valid @PathVariable int id, @Valid @RequestBody DeleteStaffRequest request) {
        log.info("Received request to delete staff with ID: {} and note: {}", id, request.note());
        ResponseData<?> result = staffService.deleteStaffById(id, request);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff deleted successfully with ID: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to delete staff with ID: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    @PutMapping("/activate-staff/{id}")
    public ResponseEntity<?> activateStaffById(@PathVariable int id, @RequestBody ActiveStaffRequest request) {
        log.info("Received request to activate staff with ID: {} and note: {}", id, request.note());
        ResponseData<?> result = staffService.activateStaffById(id, request);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff activated successfully with ID: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C204.getCode()) {
            log.warn("Staff already active with ID: {}", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        log.error("Failed to activate staff with ID: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @PutMapping("/create-university/accept/{id}")
    public ResponseEntity<?> acceptCreateUniversityRequest(@PathVariable("id") Integer id, @RequestBody CreateUniversityRequestAdminActionRequest request){
        return ResponseEntity.ok(createUniversityService.adminAction(id, CreateUniversityRequestStatus.ACCEPTED, request.note()));
    }

    @PutMapping("/create-university/reject/{id}")
    public ResponseEntity<?> rejectCreateUniversityRequest(@PathVariable("id") Integer id, @RequestBody CreateUniversityRequestAdminActionRequest request){
        return ResponseEntity.ok(createUniversityService.adminAction(id, CreateUniversityRequestStatus.REJECTED, request.note()));
    }

    @PatchMapping("/university/change-status/{id}")
    public ResponseEntity<ResponseData> activeUniversityById(@PathVariable Integer id, @RequestBody DeleteUniversityRequest request) throws ResourceNotFoundException, StoreDataFailedException {
        return ResponseEntity.ok(universityService.updateUniversityStatus(id, request.note()));
    }

}