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
public class UserFollowMajorId implements Serializable {
    private static final long serialVersionUID = -4611420264454352563L;
    @NotBlank
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotBlank
    @Column(name = "major_id", nullable = false)
    private Integer majorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFollowMajorId entity = (UserFollowMajorId) o;
        return Objects.equals(this.majorId, entity.majorId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorId, userId);
    }

}