package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.CreateConsultantRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.service.ConsultantInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/consultant")
@RequiredArgsConstructor
public class ConsultantController {
    private final ConsultantInfoService consultantInfoService;

    @PostMapping
    public ResponseEntity<ResponseData> createConsultant(@RequestBody @Valid CreateConsultantRequest request) throws DataExistedException {
        return ResponseEntity.ok(consultantInfoService.createConsultant(request));
    }
}
