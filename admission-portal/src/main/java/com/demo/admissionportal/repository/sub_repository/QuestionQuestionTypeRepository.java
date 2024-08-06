package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionQuestionType;
import com.demo.admissionportal.entity.sub_entity.id.QuestionQuestionTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionQuestionTypeRepository extends JpaRepository<QuestionQuestionType, QuestionQuestionTypeId> {
}
