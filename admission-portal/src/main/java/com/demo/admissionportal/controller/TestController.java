package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.dto.entity.create_university_request.CreateUniversityRequestDTO;
import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.impl.UniversityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final UniversityServiceImpl universityServiceImpl;
    private final CreateUniversityService createUniversityService;

    @GetMapping("/")
    public String home(){
        return "Hello home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello, Secured";
    }

    @GetMapping("/create-university-request")
    public ResponseEntity<ResponseData<Page<CreateUniversityRequestDTO>>> getCreateUniversityRequests(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String universityName,
            @RequestParam(required = false) String universityCode,
            @RequestParam(required = false) String universityEmail,
            @RequestParam(required = false) String universityUsername,
            @RequestParam(required = false) CreateUniversityRequestStatus status,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) Integer confirmBy
    ) {
        return ResponseEntity.ok(createUniversityService.getBy(
                pageable, id, universityName, universityCode, universityEmail,
                universityUsername, status, createBy, confirmBy
        ));
    }
}
