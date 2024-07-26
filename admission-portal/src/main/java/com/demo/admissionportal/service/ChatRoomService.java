package com.demo.admissionportal.service;

import java.util.Optional;
import java.util.UUID;

/**
 * <h1>Chat Room Service Interface</h1>
 * <p>
 * This interface defines the operations related to managing chat rooms within the Admission Portal.
 * It provides a method for retrieving the chat ID between two users, with an option to create a new chat
 * room if it does not already exist. Implementations of this interface are responsible for the business logic
 * associated with chat room management and interactions with the underlying data layer.
 * </p>
 * <p>
 * The following operation is supported:
 * <ul>
 *     <li>Retrieving or creating a chat room ID between two users</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
public interface ChatRoomService {
    /**
     * <h2>Get Chat ID</h2>
     * <p>
     * Retrieves the chat ID for the chat room between the specified sender and recipient. If the chat room
     * does not exist and the {@code createIfNotExist} parameter is set to {@code true}, a new chat room will be created.
     * The chat ID is represented as a {@link UUID}.
     * </p>
     *
     * @param senderId         The ID of the sender.
     * @param recipientId      The ID of the recipient.
     * @param createIfNotExist If {@code true}, a new chat room will be created if it does not already exist.
     * @return An {@link Optional} containing the chat ID if it exists, or is created successfully; otherwise, an empty {@link Optional}.
     * @since 1.0
     */
    Optional<UUID> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist);
}
