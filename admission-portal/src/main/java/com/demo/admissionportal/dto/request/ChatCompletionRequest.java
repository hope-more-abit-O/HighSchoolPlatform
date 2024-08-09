package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.entity.ChatMessage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatCompletionRequest {
    private String model;
    private List<ChatMessage> messages;

    public ChatCompletionRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<ChatMessage>();
        this.messages.add(new ChatMessage("user", prompt));
    }
}