package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Notification controller.
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Slf4j
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * Gets access token.
     *
     * @return the access token
     */
    @GetMapping("/accessToken")
    public String getAccessToken() {
        return notificationService.getAccessToken();
    }
}
