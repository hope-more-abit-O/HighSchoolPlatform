package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * The type University.
 */
@Entity
@Table(name = "university")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class University implements UserDetails {
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

    @Column(name = "address_id")
    private Integer addressId;

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

    @OneToMany(mappedBy = "university")
    @JsonIgnore
    private List<UniversityToken> tokens;

    @Column(name = "role")
    private String role;

    /**
     * Instantiates a new University.
     *
     * @param code     the code
     * @param name     the name
     * @param username the username
     * @param email    the email
     * @param password the password
     * @param type     the type
     */
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
        this.role = "UNIVERSITY";
    }

    /**
     * Instantiates a new University.
     *
     * @param code        the code
     * @param name        the name
     * @param username    the username
     * @param email       the email
     * @param description the description
     * @param password    the password
     * @param type        the type
     */
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
        this.role = "UNIVERSITY";
    }

    /**
     * Gets fail unversity.
     *
     * @return the fail unversity
     */
    public static University getFailUnversity() {
        return University.builder()
                .avatar(null)
                .id(null)
                .code(null)
                .type(UniversityType.PUBLIC)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}