package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "[consultant]")
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "email", nullable = false, length = 20)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(length = 20)
    private String avatar;

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @NotNull
    @Nationalized
    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", nullable = false, length = 255)
    private String status;


    public Consultant(String username, String name, String email, String password, String phone) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = "default_avatar.png";
        this.phone = phone;
        this.status = AccountStatus.ACTIVE.toString();
    }
}
