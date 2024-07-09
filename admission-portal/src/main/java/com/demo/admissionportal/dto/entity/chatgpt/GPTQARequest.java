package com.demo.admissionportal.dto.entity.chatgpt;

import lombok.Data;

@Data
public class GPTQARequest {
    private String question;
    private String answer;
}