package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@CrossOrigin
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @PostMapping("/createSession")
    public String createSession() {
        return aiChatService.createSession();
    }

    @PostMapping(value = "/sendMessage", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String sendMessage(@RequestParam("widget_uid") String widgetUid,
                              @RequestParam("session_uid") String sessionUid,
                              @RequestParam("message") String message) {
        return aiChatService.sendMessage(widgetUid, sessionUid, message);
    }
}
