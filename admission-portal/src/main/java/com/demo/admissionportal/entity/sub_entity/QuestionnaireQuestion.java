package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.QuestionJobId;
import com.demo.admissionportal.entity.sub_entity.id.QuestionnaireQuestionId;
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
@IdClass(QuestionnaireQuestionId.class)
@Table(name = "questionnaire_question")
public class QuestionnaireQuestion {
    @Id
    @Column(name = "questionnaire_id")
    private Integer questionnaireId;
    @Id
    @Column(name = "question_id")
    private Integer questionId;


}
