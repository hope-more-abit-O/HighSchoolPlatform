package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.TestResponseAnswer;
import com.demo.admissionportal.entity.sub_entity.id.TestResponseAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Test response answer repository.
 */
@Repository
public interface TestResponseAnswerRepository extends JpaRepository<TestResponseAnswer, TestResponseAnswerId> {
}
