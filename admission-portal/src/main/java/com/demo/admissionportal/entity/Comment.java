package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.CommentStatus;
import com.demo.admissionportal.constants.CommentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Comment.
 */
@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "comment_parent_id")
    private Integer comment_parent_id;

    @Column(name = "commenter_id")
    private Integer commenter_id;

    @Column(name = "[content]")
    private String content;

    @Column(name = "create_time")
    private Date create_time;

    @Column(name = "update_time")
    private Date update_time;

    @Column(name = "comment_type")
    @Enumerated(EnumType.STRING)
    private CommentType comment_type;

    @Column(name = "comment_status")
    @Enumerated(EnumType.STRING)
    private CommentStatus comment_status;
}
