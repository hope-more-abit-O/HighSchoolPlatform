package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.entity.chat.ChatDetailDTO;
import com.demo.admissionportal.dto.entity.chat.ChatResponseDTO;
import com.demo.admissionportal.entity.ChatNotification;
import com.demo.admissionportal.entity.UserMessage;
import com.demo.admissionportal.service.ChatRoomService;
import com.demo.admissionportal.service.UserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Process message.
     *
     * @param userMessage the user message
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload UserMessage userMessage) {
        try {
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
    public ResponseEntity<Integer> countNewMessages(@PathVariable Integer senderId, @PathVariable Integer recipientId) {
        return ResponseEntity.ok(userMessageService.countNewMessages(senderId, recipientId));
    }
}
