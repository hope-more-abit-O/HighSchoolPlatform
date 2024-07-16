package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.ChatRoom;
import com.demo.admissionportal.repository.ChatRoomRepository;
import com.demo.admissionportal.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Override
    public Optional<UUID> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(chatRoom -> UUID.fromString(chatRoom.getChatId()))
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }
                    UUID chatId = UUID.randomUUID();

                    ChatRoom senderRecipient = ChatRoom.builder()
                            .chatId(chatId.toString())
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom.builder()
                            .chatId(chatId.toString())
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
