package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.TestResponse;
import com.google.api.client.util.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

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

    /**
     * Find test response by id test response.
     *
     * @param id the id
     * @return the test response
     */
    TestResponse findTestResponseById(Integer id);
}
