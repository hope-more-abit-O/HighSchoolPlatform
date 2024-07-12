package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.service.AIChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIChatServiceImpl implements AIChatService {

    @Value("${chatbot.api.url}")
    private String apiUrl;

    @Value("${chatbot.send.api.url}")
    private String sendApiUrl;

    @Value("${chatbot.api.key}")
    private String apiKey;

    @Value("${chatbot.widget.uid}")
    private String widgetUid;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(AIChatServiceImpl.class);

    public AIChatServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String createSession() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("api-key", apiKey);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("widget_uid", widgetUid);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            logger.info("Sending request to {} with headers {}", apiUrl, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error creating session: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String sendMessage(String widgetUid, String sessionUid, String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("api-key", apiKey);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("widget_uid", widgetUid);
            body.add("session_uid", sessionUid);
            body.add("message", message);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            logger.info("Sending request to {} with headers {} and body {}", sendApiUrl, headers, body);

            ResponseEntity<String> response = restTemplate.exchange(
                    sendApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                logger.error("Unauthorized: Check the API key and permissions.");
            }

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error sending message: {}", e.getMessage());
            throw e;
        }
    }
}
