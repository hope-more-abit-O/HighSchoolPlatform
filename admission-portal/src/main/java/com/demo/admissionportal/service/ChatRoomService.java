package com.demo.admissionportal.service;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomService {
    Optional<UUID> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist);
}
