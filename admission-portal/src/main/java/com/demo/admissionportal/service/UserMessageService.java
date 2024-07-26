package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.dto.entity.chat.ChatDetailDTO;
import com.demo.admissionportal.dto.entity.chat.ChatResponseDTO;
import com.demo.admissionportal.entity.UserMessage;

import java.util.UUID;

/**
 * <h1>User Message Service Interface</h1>
 * <p>
 * This interface defines the operations related to managing user messages within the Admission Portal.
 * It provides methods for saving messages, counting new messages, retrieving chat messages, finding specific messages,
 * and updating message statuses. Implementations of this interface are responsible for the business logic
 * associated with user message management and interactions with the underlying data layer.
 * </p>
 * <p>
 * The following operations are supported:
 * <ul>
 *     <li>Saving a user message</li>
 *     <li>Counting new messages between two users</li>
 *     <li>Retrieving chat messages by chat ID</li>
 *     <li>Finding a specific message by its ID</li>
 *     <li>Updating the statuses of messages in a chat</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
public interface UserMessageService {
    /**
     * <h2>Save User Message</h2>
     * <p>
     * Saves a user message to the underlying data layer. The message contains the details of the sender, recipient,
     * content, timestamp, and status.
     * </p>
     *
     * @param message The {@link UserMessage} object containing the details of the message to be saved.
     * @return The saved {@link UserMessage} object.
     * @since 1.0
     */
    UserMessage save(UserMessage message);

    /**
     * <h2>Count New Messages</h2>
     * <p>
     * Counts the number of new messages sent by the specified sender to the specified recipient. New messages are
     * those that have not been read by the recipient.
     * </p>
     *
     * @param senderId    The ID of the sender.
     * @param recipientId The ID of the recipient.
     * @return The number of new messages.
     * @since 1.0
     */
    Integer countNewMessages(Integer senderId, Integer recipientId);

    /**
     * <h2>Find Chat Messages</h2>
     * <p>
     * Retrieves the chat messages for a specific chat room identified by the provided chat ID. The response includes
     * the details of all messages in the chat room.
     * </p>
     *
     * @param chatId The ID of the chat room.
     * @return A {@link ChatResponseDTO} object containing the details of the chat messages.
     * @since 1.0
     */
    ChatResponseDTO findChatMessages(UUID chatId);

    /**
     * <h2>Find Message By ID</h2>
     * <p>
     * Retrieves the details of a specific message identified by the provided ID. The response includes the message
     * content, sender, recipient, timestamp, and status.
     * </p>
     *
     * @param id The ID of the message to be retrieved.
     * @return A {@link ChatDetailDTO} object containing the details of the message.
     * @since 1.0
     */
    ChatDetailDTO findById(Integer id);

    /**
     * <h2>Update Message Statuses</h2>
     * <p>
     * Updates the statuses of all messages in a specific chat room identified by the provided chat ID. The status
     * is updated to the provided {@link MessageStatus} value.
     * </p>
     *
     * @param chatId The ID of the chat room.
     * @param status The new status to be applied to all messages in the chat room.
     * @since 1.0
     */
    void updateStatuses(UUID chatId, MessageStatus status);
}
