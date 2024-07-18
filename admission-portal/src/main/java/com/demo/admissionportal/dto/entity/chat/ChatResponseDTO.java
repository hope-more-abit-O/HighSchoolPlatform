package com.demo.admissionportal.dto.entity.chat;

import com.demo.admissionportal.constants.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDTO {
    private UUID chatId;
    private UserDTO senderId;
    private UserDTO receiverId;
    private List<ChatDetailDTO> chatDetail;
}