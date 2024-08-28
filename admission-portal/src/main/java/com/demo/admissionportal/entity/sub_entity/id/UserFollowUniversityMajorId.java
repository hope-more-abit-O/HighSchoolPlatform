package com.demo.admissionportal.entity.sub_entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserFollowUniversityMajorId implements Serializable {
    private static final long serialVersionUID = -1121419184526961195L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "university_major", nullable = false)
    private Integer universityMajor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFollowUniversityMajorId entity = (UserFollowUniversityMajorId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.universityMajor, entity.universityMajor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, universityMajor);
    }

}