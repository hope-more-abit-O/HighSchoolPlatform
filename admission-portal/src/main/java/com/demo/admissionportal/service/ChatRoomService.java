package com.demo.admissionportal.service;

import java.util.Optional;
import java.util.UUID;

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
    Optional<UUID> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist);
}
