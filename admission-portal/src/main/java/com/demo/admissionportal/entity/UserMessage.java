package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_messages")
public class UserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "sender_id", nullable = false)
    private Integer senderId;

    @Column(name = "recipient_id", nullable = false)
    private Integer recipientId;

    @Transient
    private String senderName;

    @Transient
    private String recipientName;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatus status;

    @ManyToOne
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", insertable = false, updatable = false)
    private User recipient;
}
