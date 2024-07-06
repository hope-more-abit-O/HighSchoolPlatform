package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;


    @GetMapping("/{id}")
    public ResponseEntity<UniversityInfoResponseDTO> findInfoUniversityById(@PathVariable Integer id) {
        return ResponseEntity.ok(universityService.getUniversityInfoResponseById(id));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseData<UniversityFullResponseDTO>> findFullUniversityById(@PathVariable Integer id) throws Exception {
        var result = ResponseData.ok("hi",universityService.getUniversityFullResponseById(id));
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<ResponseData> activeUniversityById(@PathVariable Integer id) throws ResourceNotFoundException, StoreDataFailedException{
        return ResponseEntity.ok(universityService.updateUniversityStatus(id, AccountStatus.ACTIVE));
    }

    @PatchMapping("/inactive/{id}")
    public ResponseEntity<ResponseData> inactiveUniversityById(@PathVariable Integer id) throws ResourceNotFoundException, StoreDataFailedException{
        return ResponseEntity.ok(universityService.updateUniversityStatus(id, AccountStatus.INACTIVE));
    }
}
