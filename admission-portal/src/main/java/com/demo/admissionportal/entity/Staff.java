    package com.demo.admissionportal.entity;

    import com.demo.admissionportal.constants.Role;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotNull;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.annotations.ColumnDefault;
    import org.hibernate.annotations.Nationalized;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.List;

    /**
     * The type Staff.
     */
    @Getter
    @Setter
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "[staff]")
    public class Staff implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Integer id;

        @NotNull
        @Column(name = "username")
        private String username;

        @NotNull
        @Nationalized
        @Column(name = "name")
        private String name;

        @NotNull
        @Column(name = "email")
        private String email;

        @NotNull
        @Column(name = "password")
        private String password;

        @NotNull
        @Column(name = "avatar")
        private String avatar;

        @NotNull
        @Column(name = "phone"
        )
        private String phone;

        @Enumerated(EnumType.STRING)
        @Column(name = "role")
        private Role role;

        @NotNull
        @Nationalized
        @ColumnDefault("'ACTIVE'")
        @Column(name = "status", nullable = false)
        private String status;

        @OneToMany(mappedBy = "staff")
        @JsonIgnore
        private List<StaffToken> tokens;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role.name()));
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