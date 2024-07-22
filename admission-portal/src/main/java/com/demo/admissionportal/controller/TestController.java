package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.entity.university.UniversityFullResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.UserRepository;
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

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final UniversityServiceImpl universityServiceImpl;

    @GetMapping("/")
    public String home(){
        return "Hello home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello, Secured";
    }

}
