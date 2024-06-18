package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * The type University token.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "university_token")
public class UniversityToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "university_token")
    private String universityToken;

    @NotNull
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @NotNull
    @Column(name = "expired")
    private boolean expired = false;

    @NotNull
    @Column(name = "revoked")
    private boolean revoked;

    @NotNull
    @Column(name = "refresh_token_university_token")
    private String refreshTokenUniversityToken;

    @ManyToOne
    @JoinColumn(name = "[university_id]")
    private University university;
}