package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.entity.ChatMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<ChatMessage> messages;

    public ChatGPTRequest(String model, List<ChatMessage> messages) {
        this.model = model;
        this.messages = messages;
    }
}
