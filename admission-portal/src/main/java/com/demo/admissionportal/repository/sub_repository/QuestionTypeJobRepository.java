package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.QuestionTypeJob;
import com.demo.admissionportal.entity.sub_entity.id.QuestionTypeJobId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Question type job repository.
 */
public interface QuestionTypeJobRepository extends JpaRepository<QuestionTypeJob, QuestionTypeJobId> {
    /**
     * Find by question type id list.
     *
     * @param questionTypeId the question type id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM question_type_job " +
            "WHERE question_type_id = :questionTypeId AND status = 'ACTIVE'", nativeQuery = true)
    List<QuestionTypeJob> findByQuestionTypeId(Integer questionTypeId);
}
