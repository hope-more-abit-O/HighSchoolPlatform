package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface User message repository.
 */
@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
    /**
     * Count by sender and recipient and status long.
     *
     * @param sender    the sender
     * @param recipient the recipient
     * @param status    the status
     * @return the long
     */
    Long countBySenderAndRecipientAndStatus(User sender, User recipient, MessageStatus status);

    /**
     * Find by chat id list.
     *
     * @param chatId the chat id
     * @return the list
     */
    List<UserMessage> findByChatId(String chatId);

    /**
     * Find by sender and recipient list.
     *
     * @param sender    the sender
     * @param recipient the recipient
     * @return the list
     */
    List<UserMessage> findBySenderAndRecipient(User sender, User recipient);
}
