package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.consultant.UniversityRegisterConsultantRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.ConsultantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing consultant-related operations.
 *
 * @author hopeless
 * @version 1.0
 * @since 16/06/2024
 */
@RestController
@RequestMapping("/api/v1/consultant")
@RequiredArgsConstructor
public class ConsultantController {
    private final ConsultantService consultantService;

    /**
     * Handles the registration of a new consultant for a university.
     *
     * @param request The request containing consultant information and university ID.
     * @return A response containing the registration result and consultant data.
     */
    @PostMapping
    public ResponseData<?> registerUniversity(@RequestBody @Valid UniversityRegisterConsultantRequestDTO request) {
        //if request insert null
        if (request == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage());
        }
        return consultantService.universityCreateConsultant(request);
    }
}