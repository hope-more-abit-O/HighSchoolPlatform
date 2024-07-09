package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.GPTQATraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GPTQATrainingRepository extends JpaRepository<GPTQATraining, Integer> {
    @Query(value = "SELECT * FROM gpt_qa_training gqa WHERE :question IS NULL OR gqa.question LIKE %:question%", nativeQuery = true)
    Optional<GPTQATraining> findByQuestion(String question);
}
