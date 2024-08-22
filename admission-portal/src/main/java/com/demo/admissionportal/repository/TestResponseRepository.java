package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.TestResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Test response repository.
 */
@Repository
public interface TestResponseRepository extends JpaRepository<TestResponse, Integer> {
    /**
     * Find test response by user id test response.
     *
     * @param userId the user id
     * @return the test response
     */
    TestResponse findTestResponseByUserId(Integer userId);
}
