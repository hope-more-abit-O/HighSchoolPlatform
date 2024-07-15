package com.demo.admissionportal.service;

import java.util.Optional;

public interface ChatRoomService {
    Optional<String> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist);
}
