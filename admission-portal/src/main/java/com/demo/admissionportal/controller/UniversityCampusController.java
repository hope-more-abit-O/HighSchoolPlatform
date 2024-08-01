package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.UniversityCampusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/university-campus")
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasAuthority('UNIVERSITY')")
public class UniversityCampusController {
    private final UniversityCampusService universityCampusService;

    @GetMapping()
    public ResponseEntity<ResponseData<UniversityCampusDTO>> getUniversityCampus() {
        ResponseData<UniversityCampusDTO> result = universityCampusService.getUniversityCampus();
        if (result.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
