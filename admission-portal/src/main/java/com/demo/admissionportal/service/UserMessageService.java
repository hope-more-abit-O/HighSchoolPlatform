package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.dto.entity.chat.ChatDetailDTO;
import com.demo.admissionportal.dto.entity.chat.ChatResponseDTO;
import com.demo.admissionportal.entity.UserMessage;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

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
     * Count new messages integer.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @return the integer
     */
    Integer countNewMessagesForBoth(Integer senderId, Integer recipientId);

    /**
     * Count new messages for receipient integer.
     *
     * @param recipientId the recipient id
     * @return the integer
     */
    @Transactional
    Integer countNewMessagesForReceipient(Integer recipientId);

    /**
     * Count new messages send of sender integer.
     *
     * @param senderId the sender id
     * @return the integer
     */
    @Transactional
    Integer countNewMessagesSendOfSender(Integer senderId);


    /**
     * Count new messages integer.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @return the integer
     */
    Integer countNewMessages(Integer senderId, Integer recipientId);

    /**
     * Find chat messages chat response dto.
     *
     * @param chatId the chat id
     * @return the chat response dto
     */
    ChatResponseDTO findChatMessages(UUID chatId);

    /**
     * Find by id chat detail dto.
     *
     * @param id the id
     * @return the chat detail dto
     */
    ChatDetailDTO findById(Integer id);

    /**
     * Update statuses.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @param status      the status
     */
    void updateStatuses(Integer senderId, Integer recipientId, MessageStatus status);

    /**
     * Mark messages as read.
     *
     * @param recipientId the recipient id
     * @param chatId      the chat id
     */
    void markMessagesAsRead(Integer recipientId, UUID chatId);

    /**
     * Find messages by content list.
     *
     * @param content the content
     * @return the list
     */
    @Transactional
    List<UserMessage> findMessagesByContent(String content);
}
