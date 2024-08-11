package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
//    @Query(value = "SELECT * FROM question WHERE CONTAINS(content, '\"?1\"')", nativeQuery = true)
    Question findByContent(String content);
}
