package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.dto.entity.chat.ChatDetailDTO;
import com.demo.admissionportal.dto.entity.chat.ChatResponseDTO;
import com.demo.admissionportal.entity.UserMessage;

import java.util.UUID;

public interface UserMessageService {
    UserMessage save(UserMessage message);
    Long countNewMessages(Integer senderId, Integer recipientId);
    ChatResponseDTO findChatMessages(UUID chatId);
    ChatDetailDTO findById(Integer id);
    void updateStatuses(Integer senderId, Integer recipientId, MessageStatus status);
}
