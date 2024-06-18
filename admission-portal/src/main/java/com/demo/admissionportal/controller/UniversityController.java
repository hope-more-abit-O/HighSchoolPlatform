package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.VerifyAccountRequestDTO;
import com.demo.admissionportal.dto.request.VerifyUpdateUniversityRequestDTO;
import com.demo.admissionportal.dto.request.university.StaffRegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.request.university.UpdateUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.UniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing university-related operations.
 *
 * @author hopeless
 * @version 1.0
 * @since 13 /06/2024
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

    /**
     * Update account response entity.
     *
     * @param updateUniversityRequestDTO the update university request dto
     * @return the response entity
     */
    @PutMapping()
    public ResponseEntity<ResponseData<?>> updateAccount(@RequestBody @Valid UpdateUniversityRequestDTO updateUniversityRequestDTO) {
        if (updateUniversityRequestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> verifyAccount = universityService.updateUniversity(updateUniversityRequestDTO);
        if (verifyAccount.getStatus() == ResponseCode.C206.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(verifyAccount);
        } else if (verifyAccount.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(verifyAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(verifyAccount);
    }

    /**
     * Verify update account response entity.
     *
     * @param verifyUpdateUniversityRequestDTO the verify update university request dto
     * @return the response entity
     */
    @PostMapping("/verify-update")
    public ResponseEntity<ResponseData<?>> verifyUpdateAccount(@RequestBody VerifyUpdateUniversityRequestDTO verifyUpdateUniversityRequestDTO) {
        if (verifyUpdateUniversityRequestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> verifyAccount = universityService.verifyAccount(verifyUpdateUniversityRequestDTO);
        if (verifyAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(verifyAccount);
        } else if (verifyAccount.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(verifyAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(verifyAccount);
    }
}


