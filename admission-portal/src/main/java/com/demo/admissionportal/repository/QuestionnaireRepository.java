package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The interface Questionnaire repository.
 */
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Integer> {
    /**
     * Find questionnaire page.
     *
     * @param code     the code
     * @param name     the name
     * @param status   the status
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT * " +
            "FROM questionnaire q " +
            "WHERE (:code IS NULL OR LOWER(q.questionnaire_code) LIKE LOWER(CONCAT('%', :code, '%'))) " +
            "AND   (:name IS NULL OR LOWER(q.name) LIKE LOWER(CONCAT('%', :name, '%')))" +
            "AND   (:status IS NULL OR q.status = :status)", nativeQuery = true)
    Page<Questionnaire> findQuestionnaire(@Param(value = "code") String code,
                                          @Param(value = "name") String name,
                                          @Param(value = "status") String status, Pageable pageable);
}
