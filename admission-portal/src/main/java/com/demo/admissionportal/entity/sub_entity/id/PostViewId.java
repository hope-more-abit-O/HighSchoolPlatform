package com.demo.admissionportal.entity.sub_entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * The type Post view id.
 */
@Getter
@Setter
@Embeddable
public class PostViewId implements Serializable {
    private static final long serialVersionUID = -4722529784721865529L;
    @NotNull
    @Column(name = "post_id")
    private Integer postId;

    @NotNull
    @Column(name = "create_time")
    private Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostViewId entity = (PostViewId) o;
        return Objects.equals(this.createTime, entity.createTime) &&
                Objects.equals(this.postId, entity.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createTime, postId);
    }

}