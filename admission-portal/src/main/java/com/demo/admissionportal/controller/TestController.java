package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.impl.UniversityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
