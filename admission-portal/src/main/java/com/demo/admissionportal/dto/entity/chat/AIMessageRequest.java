package com.demo.admissionportal.dto.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIMessageRequest {
    private String widget_uid;
    private String session_uid;
    private String message;
}
