package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "[consultant]")
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(length = 20)
    private String avatar;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "role")
    private String role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;


    public Consultant(String username, String name, String email, String password, String phone) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = "default_avatar.png";
        this.phone = phone;
        this.status = AccountStatus.ACTIVE;
        this.role = "CONSULTANT";
    }
}
