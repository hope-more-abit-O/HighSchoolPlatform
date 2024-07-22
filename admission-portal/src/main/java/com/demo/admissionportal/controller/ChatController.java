package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.chat.ChatDetailDTO;
import com.demo.admissionportal.dto.entity.chat.ChatResponseDTO;
import com.demo.admissionportal.entity.ChatNotification;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserMessage;
import com.demo.admissionportal.service.ChatRoomService;
import com.demo.admissionportal.service.UserMessageService;
import com.demo.admissionportal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * The type Chat controller.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/chat-user")
@CrossOrigin
public class ChatController {

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    /**
     * Process message.
     *
     * @param userMessage the user message
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload UserMessage userMessage) {
        try {
            if (!isChatAllowed(userMessage.getSenderId(), userMessage.getRecipientId())) {
                log.error("Chat not allowed between these roles.");
                return;
            }
            var chatId = chatRoomService.getChatId(userMessage.getSenderId(), userMessage.getRecipientId(), true);
            userMessage.setChatId(chatId.get().toString());

            UserMessage saved = userMessageService.save(userMessage);
            messagingTemplate.convertAndSendToUser(userMessage.getRecipientId().toString(), "/queue/messages", new ChatNotification(saved.getId().toString(), saved.getSenderId().toString(), saved.getSenderName()));
        } catch (Exception e) {
            log.error("Error processing message", e);
            throw e;
        }
    }

    /**
     * Check if chat is allowed between users based on their roles.
     *
     * @param senderId the sender id
     * @param recipientId the recipient id
     * @return boolean indicating if chat is allowed
     */
    private boolean isChatAllowed(Integer senderId, Integer recipientId) {
        User sender = userService.findById(senderId);
        User recipient = userService.findById(recipientId);
        if (sender == null || recipient == null) {
            return false;
        }
        if (sender.getRole() == Role.USER && (recipient.getRole() == Role.STAFF || recipient.getRole() == Role.CONSULTANT)) {
            return true;
        }
        if (sender.getRole() == Role.STAFF && recipient.getRole() == Role.USER) {
            return true;
        }
        if (sender.getRole() == Role.CONSULTANT && recipient.getRole() == Role.USER) {
            return true;
        }
        return false;
    }

    /**
     * Handle exception.
     *
     * @param exception the exception
     */
    @MessageExceptionHandler
    public void handleException(Throwable exception) {
        log.error("WebSocket error: ", exception);
    }

    /**
     * Find chat messages response entity.
     *
     * @param chatId the chat id
     * @return the response entity
     */
    @GetMapping("/chat-messages/{chatId}")
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT','USER')")
    public ResponseEntity<ChatResponseDTO> findChatMessages(@PathVariable UUID chatId) {
        return ResponseEntity.ok(userMessageService.findChatMessages(chatId));
    }

    /**
     * Find message response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/message/{id}")
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT','USER')")
    public ResponseEntity<ChatDetailDTO> findMessage(@PathVariable Integer id) {
        return ResponseEntity.ok(userMessageService.findById(id));
    }

    /**
     * Count new messages response entity.
     *
     * @param senderId    the sender id
     * @param recipientId the recipient id
     * @return the response entity
     */
    @GetMapping("/new-messages-count/{senderId}/{recipientId}")
    @PreAuthorize("hasAnyAuthority('STAFF','CONSULTANT','USER')")
    public ResponseEntity<Integer> countNewMessages(@PathVariable Integer senderId, @PathVariable Integer recipientId) {
        return ResponseEntity.ok(userMessageService.countNewMessagesForBoth(senderId, recipientId));
    }

    /**
     * Count new messages for receipient response entity.
     *
     * @param recipientId the recipient id
     * @return the response entity
     */
    @GetMapping("/new-messages-count/{recipientId}")
    public ResponseEntity<Integer> countNewMessagesForReceipient(@PathVariable Integer recipientId) {
        return ResponseEntity.ok(userMessageService.countNewMessagesForReceipient(recipientId));
    }

    /**
     * Count new messages send of sender response entity.
     *
     * @param senderId the sender id
     * @return the response entity
     */
    @GetMapping("/new-messages-send-count/{senderId}")
    public ResponseEntity<Integer> countNewMessagesSendOfSender(@PathVariable Integer senderId) {
        return ResponseEntity.ok(userMessageService.countNewMessagesSendOfSender(senderId));
    }

    /**
     * Find messages by content response entity.
     *
     * @param content the content
     * @return the response entity
     */
    @GetMapping("/messages/search")
    public ResponseEntity<List<UserMessage>> findMessagesByContent(@RequestParam String content) {
        return ResponseEntity.ok(userMessageService.findMessagesByContent(content));
    }
}
