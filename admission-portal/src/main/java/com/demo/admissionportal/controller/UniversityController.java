package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.university.CreateUniversityTicketRequestDTO;
import com.demo.admissionportal.dto.request.university.StaffRegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.UniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing university-related operations.
 *
 * @author hopeless
 * @version 1.0
 * @since 13/06/2024
 */
@RestController
@RequestMapping("/api/v1/unversity")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;

    /**
     * Handles the registration of a new university by a staff member.
     *
     * @param request The request containing university information.
     * @return A response containing the registration result.
     */
    @PostMapping
    public ResponseData<?> registerUniversity(@RequestBody @Valid StaffRegisterUniversityRequestDTO request) {
        //if request insert null
        if (request == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage());
        }
        return universityService.staffCreateUniversity(request);
    }

    /**
     * Creates a new university with a predefined "fail" status for testing purposes.
     *
     * @param request The request containing university information.
     * @return A response containing the creation result.
     */
    @PostMapping("/test/create/fail")
    public ResponseData<?> registerFailUniversity(@RequestBody @Valid StaffRegisterUniversityRequestDTO request) {
        //if request insert null
        if (request == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage());
        }
        return universityService.createUniversityFail(request);
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody @Valid CreateUniversityTicketRequestDTO request) {
        return null;
    }
}