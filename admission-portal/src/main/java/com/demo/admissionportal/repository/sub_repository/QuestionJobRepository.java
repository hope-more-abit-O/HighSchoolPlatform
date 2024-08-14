package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionJob;
import com.demo.admissionportal.entity.sub_entity.id.QuestionJobId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Question job repository.
 */
public interface QuestionJobRepository extends JpaRepository<QuestionJob, QuestionJobId> {
    /**
     * Find question job by question id question job.
     *
     * @param questionId the question id
     * @return the question job
     */
    List<QuestionJob> findQuestionJobByQuestionId(Integer questionId);
}
