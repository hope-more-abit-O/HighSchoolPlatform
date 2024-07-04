package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/university")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;

    @GetMapping("/{id}")
    public ResponseEntity<UniversityInfoResponseDTO> findUniversityById(@PathVariable Integer id) {
        return ResponseEntity.ok(universityService.getUniversityInfoResponseById(id));
    }
}
