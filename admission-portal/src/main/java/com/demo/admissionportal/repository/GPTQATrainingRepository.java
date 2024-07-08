package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.GPTQATraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GPTQATrainingRepository extends JpaRepository<GPTQATraining, Integer> {
    Optional<GPTQATraining> findByQuestion(String question);
}
