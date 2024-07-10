package com.demo.admissionportal.dto.entity.chat;

import com.demo.admissionportal.constants.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDTO {
    private Integer id;
    private String chatId;
    private String senderName;
    private String recipientName;
    private String content;
    private LocalDateTime timestamp;
    private MessageStatus status;
}