package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionJob;
import com.demo.admissionportal.entity.sub_entity.id.QuestionJobId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionJobRepository extends JpaRepository<QuestionJob, QuestionJobId> {
}
