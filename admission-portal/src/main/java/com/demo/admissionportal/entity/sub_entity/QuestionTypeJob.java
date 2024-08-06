package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.QuestionTypeJobId;
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
@IdClass(QuestionTypeJobId.class)
public class QuestionTypeJob {
    @Id
    @Column(name = "question_type_id")
    private Integer questionTypeId;
    @Id
    @Column(name = "job_id")
    private Integer jobId;
}
