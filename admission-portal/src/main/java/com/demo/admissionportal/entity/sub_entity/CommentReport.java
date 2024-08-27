package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.constants.PostReportActionType;
import com.demo.admissionportal.constants.isBannedType;
import com.demo.admissionportal.entity.sub_entity.id.CommentReportId;
import com.demo.admissionportal.entity.sub_entity.id.PostReportId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CommentReportId.class)
@Table(name = "comment_report_detail")
public class CommentReport {
    @Id
    @Column(name = "report_id")
    private Integer reportId;

    @Id
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "content")
    private String commentContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_action")
    private PostReportActionType reportAction;

    @Enumerated(EnumType.STRING)
    @Column(name = "isBanned")
    private isBannedType isBanned;
}
