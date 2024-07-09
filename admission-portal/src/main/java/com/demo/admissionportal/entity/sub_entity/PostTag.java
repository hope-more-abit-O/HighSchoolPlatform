package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.Tag;
import com.demo.admissionportal.entity.sub_entity.id.PostTagId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Post tag.
 */
@Getter
@Setter
@Entity
@Table(name = "post_tag")
public class PostTag {
    @EmbeddedId
    private PostTagId id;

    @MapsId("postId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @MapsId("tagId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostPropertiesStatus status;

}