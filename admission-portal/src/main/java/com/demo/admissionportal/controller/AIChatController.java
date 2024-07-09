package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.entity.chatgpt.GPTQARequest;
import com.demo.admissionportal.entity.GPTQATraining;
import com.demo.admissionportal.service.AIChatService;
import com.demo.admissionportal.service.GPTQATrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @Autowired
    private GPTQATrainingService gptqaTrainingService;

    @PostMapping
    public String chat(@RequestBody String prompt) {
        return aiChatService.getChatResponse(prompt);
    }

    @PostMapping("/question-answer-gpt")
    public GPTQATraining addGQA(@RequestBody GPTQARequest gptqaRequest) {
        return gptqaTrainingService.addQA(gptqaRequest.getQuestion(), gptqaRequest.getAnswer());
    }
}
