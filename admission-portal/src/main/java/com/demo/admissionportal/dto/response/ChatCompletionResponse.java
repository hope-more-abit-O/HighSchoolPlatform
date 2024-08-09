package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionResponse {
    private List<Choice> choices;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {
        private Integer index;
        private ChatMessage message;
    }
}