package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * The type User token.
 */
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

    @NotBlank
    @Column(name = "token")
    private String token;

    @NotBlank
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @NotBlank
    @Column(name = "expired")
    private boolean expired;

    @NotBlank
    @Column(name = "revoked")
    private boolean revoked;

    @NotBlank
    @Column(name = "refresh_token")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "[user_id]")
    private User user;
}