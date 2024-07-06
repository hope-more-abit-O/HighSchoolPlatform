package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.ChatCompletionRequest;
import com.demo.admissionportal.dto.response.ChatCompletionResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin
public class AIChatController {
//    @Autowired
//    private AIService aiService;

    @Autowired
    RestTemplate restTemplate;

//    @GetMapping("/joke")
//    public String getJoke(@RequestParam String topic){
//        return aiService.getJoke(topic);
//    }
//
//    @GetMapping("/books")
//    public String getBooks(@RequestParam String category, @RequestParam String year){
//        return aiService.getBooks(category, year);
//    }

    @PostMapping("/prompt")
    public String getOpenAIResponse(@RequestBody @Valid String prompt){
        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest("gpt-3.5-turbo", prompt);
        ChatCompletionResponse chatCompletionResponse = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", chatCompletionRequest, ChatCompletionResponse.class);
        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
    }
}
