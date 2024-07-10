package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.dto.entity.chat.UserMessageDTO;
import com.demo.admissionportal.entity.UserMessage;

import java.util.List;

/**
 * The interface User message service.
 */
public interface UserMessageService {
    /**
     * Save user message.
     *
     * @param message the message
     * @return the user message
     */
    UserMessage save(UserMessage message);

    /**
     * Count new messages long.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @return the long
     */
    Long countNewMessages(Integer senderId, Integer recipientId);

    /**
     * Find chat messages list.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @return the list
     */
    List<UserMessageDTO> findChatMessages(Integer senderId, Integer recipientId);

    /**
     * Find by id user message dto.
     *
     * @param id the id
     * @return the user message dto
     */
    UserMessageDTO findById(Integer id);

    /**
     * Update statuses.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @param status      the status
     */
    void updateStatuses(Integer senderId, Integer recipientId, MessageStatus status);
}
