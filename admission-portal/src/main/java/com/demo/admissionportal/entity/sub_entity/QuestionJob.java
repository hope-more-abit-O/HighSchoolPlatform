package com.demo.admissionportal.entity.sub_entity;


import com.demo.admissionportal.constants.QuestionStatus;
import com.demo.admissionportal.entity.sub_entity.id.QuestionJobId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(QuestionJobId.class)
@Table(name = "question_job")
public class QuestionJob {
    @Id
    @Column(name = "job_id")
    private Integer jobId;
    @Id
    @Column(name = "question_id")
    private Integer questionId;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_by")
    private Integer updateBy;
    @Column(name = "update_time")
    private Date updateTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private QuestionStatus status;

}
