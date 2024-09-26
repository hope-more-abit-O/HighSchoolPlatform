package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.notification.NotificationRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping("/send")
    public ResponseData<String> sendNotification(@RequestBody NotificationRequest request) throws IOException {
        return notificationService.sendNotification(request);
    }
}
