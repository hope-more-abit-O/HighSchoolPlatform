package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * The type Staff token.
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staff_token")
public class StaffToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "staff_token")
    private String staffToken;

    @NotNull
    @Column(name = "token_type")
    private TokenType tokenType;

    @NotNull
    @Column(name = "expired")
    private boolean expired = false;

    @NotNull
    @Column(name = "revoked")
    private boolean revoked = false;

    @NotNull
    @Column(name = "refresh_token_staff_token")
    private String refreshTokenStaffToken;

    @ManyToOne
    @JoinColumn(name = "[staff_id]")
    private Staff staff;
}