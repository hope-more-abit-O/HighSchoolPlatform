package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.PostStatus;
import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.entity.sub_entity.PostTag;
import com.demo.admissionportal.entity.sub_entity.PostType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * The type Post.
 */
@Getter
@Setter
@Entity
@Table(name = "post")
@SqlResultSetMapping(
        name = "PostSearchDTOResult",
        classes = @ConstructorResult(
                targetClass = PostSearchDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "title", type = String.class),
                        @ColumnResult(name = "createTime", type = Date.class),
                        @ColumnResult(name = "quote", type = String.class),
                        @ColumnResult(name = "thumnail", type = String.class),
                        @ColumnResult(name = "url", type = String.class),
                        @ColumnResult(name = "createBy", type = Integer.class),
                        @ColumnResult(name = "avatar", type = String.class),
                        @ColumnResult(name = "status", type = PostStatus.class)
                }
        )
)
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

    @Column(name = "[like]")
    private Integer like;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostTag> postTags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostType> postTypes;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "url")
    private String url;

    @Column(name = "note")
    private String note;

    @Column(name = "user_report_post_result")
    private boolean userReportPostResult = false;

    public void userReportPostResult(boolean userReportPostResult) {
        this.userReportPostResult = userReportPostResult;
    }

}