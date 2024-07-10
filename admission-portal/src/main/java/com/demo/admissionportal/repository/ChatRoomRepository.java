package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Chat room repository.
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    /**
     * Find by sender id and recipient id optional.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @return the optional
     */
    Optional<ChatRoom> findBySenderIdAndRecipientId(Integer senderId, Integer recipientId);
}
