package com.demo.admissionportal.service;

import java.util.Optional;

/**
 * The interface Chat room service.
 */
public interface ChatRoomService {
    /**
     * Gets chat id.
     *
     * @param senderId         the sender id
     * @param recipientId      the recipient id
     * @param createIfNotExist the create if not exist
     * @return the chat id
     */
    Optional<String> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist);
}
