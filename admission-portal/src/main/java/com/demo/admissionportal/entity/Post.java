package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.PostStatus;
import com.demo.admissionportal.entity.sub_entity.PostTag;
import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.entity.sub_entity.PostView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

/**
 * The type Post.
 */
@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "thumnail")
    private String thumnail;


    @Column(name = "quote")
    private String quote;

    @Column(name = "[view]")
    private Integer view;

    @Column(name = "[like]")
    private Integer like;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @ColumnDefault("NULL")
    @Column(name = "update_by")
    private Integer updateBy;

    @OneToOne(mappedBy = "post")
    private PostTag postTags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostType> postTypes;

    @OneToOne(mappedBy = "post")
    private PostView postViews;

    @Column(name = "create_by")
    private Integer createBy;
}