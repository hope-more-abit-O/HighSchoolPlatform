package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Student token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "student_token")
public class StudentToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "token_student")
    private String tokenStudent;

    @NotNull
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @NotNull
    @Column(name = "expired", nullable = false)
    private boolean expired;

    @NotNull
    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @NotNull
    @Column(name = "refresh_token_student")
    private String refreshTokenStudent;

    @ManyToOne
    @JoinColumn(name = "[student_id]")
    private Student student;
}