package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ProviderType;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.resetPassword.ResetPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The type User.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "[user]")
public class User implements UserDetails, ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @NotNull
    @ColumnDefault("'ACTIVE'")
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserToken> tokens;

    @Column(name = "note")
    private String note;

    @Override
    public String getEmail() {
        return email;
    }


    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Role getRole() {
        return role;
    }


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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

    public User(String username, String email, String password, Role role, Integer createBy){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createBy = createBy;
        this.updateBy = null;
        this.createTime = new Date();
        this.updateTime = null;
        if (role == Role.STAFF) {
            this.avatar = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/uploads%2Fstaff.png?alt=media&token=a69428ef-365a-46fc-8d6b-8b8769e6c96b";
        } else if (role == Role.USER) {
            this.avatar = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/uploads%2Fstudent.png?alt=media&token=c13af7ac-f8c3-4eac-b98a-6e9dd9a191fd";
        } else if (role == Role.CONSULTANT) {
            this.avatar = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/uploads%2Fconsultant.png?alt=media&token=861173d6-24a9-4dc9-b572-87f987b5f1b6";
        } else if (role == Role.UNIVERSITY) {
            this.avatar = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/uploads%2Funiversity.png?alt=media&token=d80d250a-c6c5-406b-9314-cbf9b7da9c2d";
        }
        this.status = AccountStatus.ACTIVE;
    }
}