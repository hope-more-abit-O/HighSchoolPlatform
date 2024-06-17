package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * The type Admin token.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin_token")
public class AdminToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "admin_token")
    private String adminToken;

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
    @Column(name = "refresh_token_admin_token")
    private String refreshTokenAdminToken;

    @ManyToOne
    @JoinColumn(name = "[admin_id]")
    private Admin admin;
}