package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.TestResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    List<TestResponse> findTestResponseByUserId(Integer userId);

    /**
     * Find test response by id test response.
     *
     * @param id the id
     * @return the test response
     */
    TestResponse findTestResponseById(Integer id);
}
