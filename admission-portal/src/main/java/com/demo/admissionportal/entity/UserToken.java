package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "[user_token]")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "token")
    private String token;

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
    @Column(name = "refresh_token")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "[user_id]")
    private User user;
}
