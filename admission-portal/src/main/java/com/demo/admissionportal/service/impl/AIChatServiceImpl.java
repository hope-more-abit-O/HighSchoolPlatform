package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.request.ChatGPTRequest;
import com.demo.admissionportal.dto.response.ChatGPTResponse;
import com.demo.admissionportal.service.AIChatService;
import com.demo.admissionportal.service.GPTQATrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@Service
public class AIChatServiceImpl implements AIChatService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate template;

    @Autowired
    private GPTQATrainingService gptqaTrainingService;

    @Override
    public String getChatResponse(String prompt) {
        // Debug log for incoming prompt
        System.out.println("Received prompt: " + prompt);

        // Extract the prompt from JSON object
        String extractedPrompt = extractPromptFromJson(prompt);

        // Check if the question matches a predefined QA
        Optional<String> newAnswer = gptqaTrainingService.getAnswer(extractedPrompt);
        if (newAnswer.isPresent()) {
            // Debug log for found answer
            System.out.println("Found predefined answer: " + newAnswer.get());
            return newAnswer.get();
        } else {
            // Debug log for fallback to OpenAI's ChatGPT
            System.out.println("No predefined answer found, falling back to OpenAI's ChatGPT");

            // If not, fallback to OpenAI's ChatGPT
            ChatGPTRequest request = new ChatGPTRequest(model, extractedPrompt);
            ChatGPTResponse chatGPTResponse = template.postForObject(apiUrl, request, ChatGPTResponse.class);

            // Debug log for OpenAI's ChatGPT response
            System.out.println("ChatGPT response: " + chatGPTResponse.getChoices().get(0).getMessage().getContent());

            return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        }
    }

    private String extractPromptFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            return jsonNode.get("prompt").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return json;
        }
    }
}
