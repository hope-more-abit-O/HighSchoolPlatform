package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.entity.sub_entity.id.PostTypeId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

/**
 * The type Post type.
 */
@Getter
@Setter
@Entity
@Table(name = "post_type")
public class PostType {
    @EmbeddedId
    private PostTypeId id;

    @MapsId("postId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @MapsId("typeId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private Type type;

    @NotNull
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @NotNull
    @Nationalized
    @Column(name = "status")
    private String status;

}