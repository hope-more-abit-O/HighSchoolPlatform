package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.CreateUniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing university creation requests.
 *
 * <p> This controller provides endpoints for handling university creation requests
 * submitted by staff users. It includes functionality for creating, accepting,
 * and rejecting these requests.
 *
 * @see CreateUniversityService
 */
@RestController
@RequestMapping("/api/v1/create-university")
@RequiredArgsConstructor
public class CreateUniversityController {
}
