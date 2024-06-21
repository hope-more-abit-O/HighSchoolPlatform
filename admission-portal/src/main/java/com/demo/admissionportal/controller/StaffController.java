package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.StaffResponseDTO;
import com.demo.admissionportal.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * The type Staff controller.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/staffs")
@Slf4j
public class StaffController {
    @Autowired
    private StaffService staffService;
    /**
     * Register staff response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseData<?>> registerStaff(@RequestBody @Valid RegisterStaffRequestDTO request) {
        //if request insert null
        if (request == null || request.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage()));
        }
        ResponseData<?> result = staffService.registerStaff(request);
        //if request full field
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
            //if duplicate field
        } else if (result.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        //others
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    @PutMapping("/update/{id}")
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

    @GetMapping("/list-all")
    public ResponseEntity<ResponseData<Page<StaffResponseDTO>>> findAllStaff(
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        log.info("Get all staff with filters: Username: {}, Name: {}, Email: {}, Phone: {}, Page: {}, Size: {}, SortBy: {}",
                username, name, email, phone, page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        ResponseData<Page<StaffResponseDTO>> result = staffService.findAll(username, name, email, phone, pageable);

        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    @GetMapping("/{id}")
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<?>> deleteStaffById(@PathVariable int id) {
        log.info("Request to delete staff by ID: {}", id);
        ResponseData<?> result = staffService.deleteStaffById(id);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Staff deleted successfully: {}", id);
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Staff not found by ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to delete staff: {}", id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

}
