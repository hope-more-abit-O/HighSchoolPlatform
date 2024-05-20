package com.demo.admissionportal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Demo controller.
 */
@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    /**
     * Demo controller response entity.
     *
     * @return the response entity
     */
    @GetMapping()
    public ResponseEntity<String> demoController() {
        return ResponseEntity.ok("Hello World");
    }
}
