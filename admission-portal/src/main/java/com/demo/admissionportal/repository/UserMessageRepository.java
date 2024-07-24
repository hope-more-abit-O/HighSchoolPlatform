package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
    Integer countBySenderAndRecipientAndStatus(User sender, User recipient, MessageStatus status);
    List<UserMessage> findByChatId(String chatId);
    List<UserMessage> findBySenderAndRecipient(User sender, User recipient);
}