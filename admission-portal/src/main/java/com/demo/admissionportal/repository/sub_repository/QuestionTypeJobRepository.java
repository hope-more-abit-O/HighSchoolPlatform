package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionTypeJob;
import com.demo.admissionportal.entity.sub_entity.id.QuestionTypeJobId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionTypeJobRepository extends JpaRepository<QuestionTypeJob, QuestionTypeJobId> {
}
