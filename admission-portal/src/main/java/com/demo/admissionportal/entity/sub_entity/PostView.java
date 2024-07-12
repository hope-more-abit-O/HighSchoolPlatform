package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Post view.
 */
@Getter
@Setter
@Entity
@Table(name = "post_view")
public class PostView {
    @Id
    @Column(name = "post_id")
    private Integer id;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "like_count")
    private Integer likeCount;

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