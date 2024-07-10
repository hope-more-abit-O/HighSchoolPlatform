package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.request.ChatGPTRequest;
import com.demo.admissionportal.dto.response.ChatGPTResponse;
import com.demo.admissionportal.entity.ChatMessage;
import com.demo.admissionportal.service.AIChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIChatServiceImpl implements AIChatService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate template;

    @Override
    public String getChatResponse(String prompt) {
        // Create the messages list
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are a helpful assistant."));
        messages.add(new ChatMessage("user", prompt));

        // Create the request object
        ChatGPTRequest request = new ChatGPTRequest(model, messages);

        // Send the request to the OpenAI API
        ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, request, ChatGPTResponse.class);

        // Extract the response content
        String responseContent = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        System.out.println("ChatGPT response: " + responseContent);

        return responseContent;
    }
}
