package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Size(max = 20)
    @NotNull
    @Column(name = "email", nullable = false, length = 20)
    private String email;

    @Size(max = 100)
    @NotNull
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Size(max = 20)
    @NotNull
    @Column(name = "avatar", nullable = false, length = 20)
    private String avatar;

    @Size(max = 11)
    @NotNull
    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", nullable = false)
    private String status;

}