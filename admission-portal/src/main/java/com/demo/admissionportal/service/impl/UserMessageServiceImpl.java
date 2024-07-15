package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.dto.entity.chat.UserMessageDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserMessage;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.repository.UserMessageRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.ChatRoomService;
import com.demo.admissionportal.service.UserMessageService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public UserMessage save(UserMessage message) {
        message.setStatus(MessageStatus.DELIVERED);
        message.setTimestamp(LocalDateTime.now());
        return userMessageRepository.save(message);
    }

    @Override
    @Transactional
    public Long countNewMessages(Integer senderId, Integer recipientId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        return userMessageRepository.countBySenderAndRecipientAndStatus(sender, recipient, MessageStatus.RECEIVED);
    }

    @Override
    @Transactional
    public List<UserMessageDTO> findChatMessages(Integer senderId, Integer recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages = chatId.map(cId -> userMessageRepository.findByChatId(cId)).orElse(new ArrayList<>());

        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        messages.forEach(message -> {
            if (message.getRecipient().equals(recipient) && message.getStatus() == MessageStatus.DELIVERED) {
                message.setStatus(MessageStatus.RECEIVED);
            }
        });
        userMessageRepository.saveAll(messages);

        return messages.stream().map(message -> {
            UserMessageDTO userMessage = modelMapper.map(message, UserMessageDTO.class);
            userMessage.setSenderName(message.getSender().getUsername());
            userMessage.setRecipientName(message.getRecipient().getUsername());
            return userMessage;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserMessageDTO findById(Integer id) {
        return userMessageRepository.findById(id).map(message -> {
            if (message.getStatus() == MessageStatus.DELIVERED) {
                message.setStatus(MessageStatus.RECEIVED);
                userMessageRepository.save(message);
            }
            UserMessageDTO userMessage = modelMapper.map(message, UserMessageDTO.class);
            userMessage.setSenderName(message.getSender().getUsername());
            userMessage.setRecipientName(message.getRecipient().getUsername());
            return userMessage;
        }).orElseThrow(() -> new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    @Override
    @Transactional
    public void updateStatuses(Integer senderId, Integer recipientId, MessageStatus status) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        List<UserMessage> messages = userMessageRepository.findBySenderAndRecipient(sender, recipient);
        for (UserMessage message : messages) {
            message.setStatus(status);
        }
        userMessageRepository.saveAll(messages);
    }
}
