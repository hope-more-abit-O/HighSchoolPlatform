package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * The type Student.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "student")
public class Student implements UserDetails {
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
    @Column(name = "firstname", nullable = false, length = 20)
    private String firstname;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "middle_name", nullable = false, length = 20)
    private String middleName;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false, length = 20)
    private String email;

    @Size(max = 100)
    @NotNull
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @NotNull
    @Column(name = "address_id", nullable = false)
    private Integer addressId;

    @NotNull
    @Column(name = "birthday", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime birthday;

    @Size(max = 10)
    @NotNull
    @Nationalized
    @Column(name = "education_level", nullable = false, length = 10)
    private String educationLevel;

    @Size(max = 20)
    @NotNull
    @Column(name = "avatar", nullable = false, length = 20)
    private String avatar;

    @Size(max = 11)
    @NotNull
    @Column(name = "phone", nullable = false, length = 11)
    private String phone;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @ColumnDefault("MALE")
    @Column(name = "gender", nullable = false)
    private String gender;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @ColumnDefault("ACTIVE")
    @Column(name = "status", nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<StudentToken> tokens;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}