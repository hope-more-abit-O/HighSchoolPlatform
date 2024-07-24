package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.PostReportId;
import com.demo.admissionportal.entity.sub_entity.id.StudentReportMarkId;
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
@IdClass(PostReportId.class)
@Table(name = "post_report")
public class PostReport {
    @Id
    @Column(name = "report_id")
    private Integer reportId;
    @Id
    @Column(name = "post_id")
    private Integer postId;
    @Column(name = "report_action")
    private String report_action;
}