package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.entity.chat.UserMessageDTO;
import com.demo.admissionportal.entity.UserMessage;
import com.demo.admissionportal.service.ChatRoomService;
import com.demo.admissionportal.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-user")
public class ChatController {

    @Autowired
    private UserMessageService messageService;

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/messages")
    public ResponseEntity<UserMessage> processMessage(@RequestBody UserMessage message) {
        UserMessage saved = messageService.save(message);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<UserMessageDTO>> findChatMessages(
            @PathVariable Integer senderId,
            @PathVariable Integer recipientId) {
        return ResponseEntity.ok(messageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<UserMessageDTO> findMessage(@PathVariable Integer id) {
        return ResponseEntity.ok(messageService.findById(id));
    }
}
