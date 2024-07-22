package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.MessageStatus;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.chat.ChatDetailDTO;
import com.demo.admissionportal.dto.entity.chat.ChatResponseDTO;
import com.demo.admissionportal.dto.entity.chat.UserDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.UserMessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private StaffInfoRepository staffInfoRepository;

    @Autowired
    private ConsultantInfoRepository consultantInfoRepository;

    @Override
    @Transactional
    public UserMessage save(UserMessage message) {
        message.setStatus(MessageStatus.DELIVERED);
        message.setTimestamp(LocalDateTime.now());
        return userMessageRepository.save(message);
    }

    @Override
    @Transactional
    public Integer countNewMessagesForBoth(Integer senderId, Integer recipientId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        return userMessageRepository.countBySenderAndRecipientAndStatus(sender, recipient, MessageStatus.RECEIVED);
    }

    @Override
    @Transactional
    public Integer countNewMessagesForReceipient(Integer recipientId) {
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        return userMessageRepository.countByRecipientAndStatus(recipient, MessageStatus.RECEIVED);
    }

    @Override
    @Transactional
    public Integer countNewMessagesSendOfSender(Integer recipientId) {
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        return userMessageRepository.countBySenderAndStatus(recipient, MessageStatus.RECEIVED);
    }

    @Override
    @Transactional
    public Integer countNewMessages(Integer senderId, Integer recipientId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        return userMessageRepository.countBySenderAndRecipientAndStatus(sender, recipient, MessageStatus.RECEIVED);
    }


    @Override
    @Transactional
    public ChatResponseDTO findChatMessages(UUID chatId) {
        var messages = userMessageRepository.findByChatId(chatId.toString());

        if (messages.isEmpty()) {
            throw new ResourceNotFoundException("Chat ID not found");
        }

        User sender = userRepository.findById(messages.get(0).getSenderId()).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User recipient = userRepository.findById(messages.get(0).getRecipientId()).orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        List<ChatDetailDTO> chatDetails = messages.stream().map(message -> {
            String type = message.getContent().startsWith("http") ? "IMAGE" : "TEXT";
            return ChatDetailDTO.builder().senderId(message.getSenderId()).content(message.getContent()).type(type).time(new Date()).build();
        }).collect(Collectors.toList());
        markMessagesAsRead(recipient.getId(), chatId);
        return ChatResponseDTO.builder()
                .chatId(chatId)
                .senderId(
                        UserDTO.builder()
                                .id(sender.getId())
                                .name(getNameOfUser(sender))
                                .avatar(sender.getAvatar())
                                .build())
                .receiverId(UserDTO.builder()
                        .id(recipient.getId())
                        .name(getNameOfUser(recipient))
                        .avatar(recipient.getAvatar())
                        .build())
                .chatDetail(chatDetails).build();
    }

    @Override
    @Transactional
    public ChatDetailDTO findById(Integer id) {
        return userMessageRepository.findById(id).map(message -> {
            if (message.getStatus() == MessageStatus.DELIVERED) {
                message.setStatus(MessageStatus.RECEIVED);
                userMessageRepository.save(message);
            }
            return ChatDetailDTO.builder().senderId(message.getSenderId()).content(message.getContent()).type(message.getContent().startsWith("http") ? "IMAGE" : "TEXT").time(new Date()).build();
        }).orElseThrow(() -> new ResourceNotFoundException("Can't find message (" + id + ")"));
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

    @Override
    @Transactional
    public void markMessagesAsRead(Integer recipientId, UUID chatId) {
        List<UserMessage> messages = userMessageRepository.findByChatIdAndRecipientId(chatId.toString(), recipientId);
        for (UserMessage message : messages) {
            if (message.getStatus() == MessageStatus.DELIVERED) {
                message.setStatus(MessageStatus.RECEIVED);
            }
        }
        userMessageRepository.saveAll(messages);
    }

    @Override
    @Transactional
    public List<UserMessage> findMessagesByContent(String content) {
        return userMessageRepository.findByContentContaining(content);
    }

    private String getNameOfUser(User user) {
        String fullName = "Người dùng UAPortal";

        if (user.getRole() == Role.USER) {
            UserInfo userInfo = userInfoRepository.findById(user.getId()).orElse(null);
            if (userInfo != null) {
                fullName = getFullName(userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName());
            }
        } else if (user.getRole() == Role.STAFF) {
            StaffInfo staffInfo = staffInfoRepository.findById(user.getId()).orElse(null);
            if (staffInfo != null) {
                fullName = getFullName(staffInfo.getFirstName(), staffInfo.getMiddleName(), staffInfo.getLastName());
            }
        } else if (user.getRole() == Role.CONSULTANT) {
            ConsultantInfo consultantInfo = consultantInfoRepository.findById(user.getId()).orElse(null);
            if (consultantInfo != null) {
                fullName = getFullName(consultantInfo.getFirstName(), consultantInfo.getMiddleName(), consultantInfo.getLastName());
            }
        }

        return fullName;
    }

    private String getFullName(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            fullName.append(" ").append(middleName);
        }
        fullName.append(" ").append(lastName);
        return fullName.toString();
    }
}
