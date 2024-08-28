package com.demo.admissionportal.entity.sub_entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserFavoriteId implements Serializable {
    private static final long serialVersionUID = -2688869997617000904L;
    @NotBlank
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotBlank
    @Column(name = "university_id", nullable = false)
    private Integer universityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFavoriteId entity = (UserFavoriteId) o;
        return Objects.equals(this.universityId, entity.universityId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(universityId, userId);
    }

}