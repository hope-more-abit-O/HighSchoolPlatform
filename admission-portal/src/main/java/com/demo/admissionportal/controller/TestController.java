package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.CreateUniversityService;
import com.demo.admissionportal.service.impl.UniversityServiceImpl;
import com.demo.admissionportal.service.impl.ValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final UniversityServiceImpl universityServiceImpl;
    private final CreateUniversityService createUniversityService;
    private final ValidationServiceImpl validationServiceImpl;

    @GetMapping("/")
    public String home(){
        return "Hello home";
    }

    @GetMapping("/secured")
    public String secured(){
        return "Hello, Secured";
    }

    @GetMapping("/validate-email/{email}")
    public Boolean validateEmail(@PathVariable String email){
        return validationServiceImpl.validateEmail(email);
    }
}
