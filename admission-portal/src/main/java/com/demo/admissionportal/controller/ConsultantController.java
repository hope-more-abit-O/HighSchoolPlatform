package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.consultant.SelfUpdateConsultantInfoRequest;
import com.demo.admissionportal.dto.request.consultant.UpdateConsultantAddressRequest;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.service.ConsultantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling endpoints related to consultant management.
 */
@RestController
@RequestMapping("/api/v1/consultant")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasAuthority('CONSULTANT')")
public class ConsultantController {
    private final ConsultantService consultantService;

    /**
     * Retrieves details of a consultant by their ID.
     *
     * <p>Fetches detailed information about a consultant using
     * their unique identifier and returns the data as a response.
     *
     * @return     A ResponseEntity containing consultant details with
     *             a suitable HTTP status (e.g., 200 OK for success, 404 Not Found
     *             if the consultant is not found).
     */
    @GetMapping("/info")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> getselfInfo() {
        return ResponseEntity.ok(consultantService.getSelfInfo());
    }

    //TODO: JAVADOC
    @PutMapping("/update/info")
    public ResponseEntity<?> seftUpdateConsultantInfo(@RequestBody @Valid SelfUpdateConsultantInfoRequest request)
            throws ResourceNotFoundException, StoreDataFailedException{
        return ResponseEntity.ok(consultantService.selfUpdateConsultantInfo(request));
    }

    @PutMapping("/update/address")
    public ResponseEntity<?> seftUpdateConsultantAddress(@RequestBody @Valid UpdateConsultantAddressRequest request)
            throws ResourceNotFoundException, StoreDataFailedException{
        return ResponseEntity.ok(consultantService.selfUpdateConsultantAddress(request));
    }
}
