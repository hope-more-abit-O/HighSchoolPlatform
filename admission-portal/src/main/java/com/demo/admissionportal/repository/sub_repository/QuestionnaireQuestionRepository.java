package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionnaireQuestion;
import com.demo.admissionportal.entity.sub_entity.id.QuestionnaireQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireQuestionRepository extends JpaRepository<QuestionnaireQuestion, QuestionnaireQuestionId> {
}
