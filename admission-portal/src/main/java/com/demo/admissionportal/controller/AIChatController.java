package com.demo.admissionportal.controller;

import com.demo.admissionportal.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @PostMapping
    public String chat(@RequestBody String prompt) {
        return aiChatService.getChatResponse(prompt);
    }

}