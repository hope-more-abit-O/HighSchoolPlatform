package com.demo.admissionportal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Chat notification.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private String id;
    private String senderId;
    private String senderName;
}