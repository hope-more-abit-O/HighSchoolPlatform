package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UniversityType type;

    @NotNull
    @Nationalized
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    public University(String code, String name, String username, String email, String password, UniversityType type) {
        this.code = code;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = "Hãy thêm miêu tả về trường của bạn đây";
        this.type = type;
        this.status = AccountStatus.PENDING;
        this.avatar = "unversity_avatar.png";
    }
    public University(String code, String name, String username, String email, String description, String password, UniversityType type) {
        this.code = code;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = description;
        this.type = type;
        this.status = AccountStatus.PENDING;
        this.avatar = "unversity_avatar.png";
    }

    public static University getFailUnversity(){
        return University.builder()
                .avatar(null)
                .id(null)
                .code(null)
                .type(UniversityType.PUBLIC)
                .build();
    }
}