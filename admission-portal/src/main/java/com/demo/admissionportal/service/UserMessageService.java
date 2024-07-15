package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.dto.entity.chat.UserMessageDTO;
import com.demo.admissionportal.entity.UserMessage;

import java.util.List;

public interface UserMessageService {
    UserMessage save(UserMessage message);
    Long countNewMessages(Integer senderId, Integer recipientId);
    List<UserMessageDTO> findChatMessages(Integer senderId, Integer recipientId);
    UserMessageDTO findById(Integer id);
    void updateStatuses(Integer senderId, Integer recipientId, MessageStatus status);
}
