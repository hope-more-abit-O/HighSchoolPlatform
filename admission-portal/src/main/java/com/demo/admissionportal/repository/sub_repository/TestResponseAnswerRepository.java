package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.TestResponseAnswer;
import com.demo.admissionportal.entity.sub_entity.id.TestResponseAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Test response answer repository.
 */
@Repository
public interface TestResponseAnswerRepository extends JpaRepository<TestResponseAnswer, TestResponseAnswerId> {
    /**
     * Find test response answer by test response id list.
     *
     * @param testResponseId the test response id
     * @return the list
     */
    List<TestResponseAnswer> findTestResponseAnswerByTestResponseId(Integer testResponseId);
}
