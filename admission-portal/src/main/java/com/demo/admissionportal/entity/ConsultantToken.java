package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consultant_token")
public class ConsultantToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "consultant_token")
    private String consultantToken;

    @NotNull
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @NotNull
    @Column(name = "expired")
    private boolean expired;

    @NotNull
    @Column(name = "revoked")
    private boolean revoked;

    @NotNull
    @Column(name = "refresh_token_consultant_token")
    private String refreshTokenConsultantToken;

    @ManyToOne
    @JoinColumn(name = "[consultant_id]")
    private Consultant consultant;
}