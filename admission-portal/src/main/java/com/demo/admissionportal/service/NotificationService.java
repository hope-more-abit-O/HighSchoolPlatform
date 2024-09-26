package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.notification.NotificationRequest;
import com.demo.admissionportal.dto.response.ResponseData;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * The interface Notification service.
 */
public interface NotificationService {
    /**
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken();

    /**
     * Send notification response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<String> sendNotification(NotificationRequest request) throws IOException;
}
