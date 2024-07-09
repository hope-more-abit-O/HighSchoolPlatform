package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.sub_entity.id.PostViewId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

/**
 * The type Post view.
 */
@Getter
@Setter
@Entity
@Table(name = "post_view")
public class PostView {
    @EmbeddedId
    private PostViewId id;

    @MapsId("postId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Integer createTime;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private Integer viewCount;

    @ColumnDefault("0")
    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @NotNull
    @Nationalized
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostPropertiesStatus status;

}