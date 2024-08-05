package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
