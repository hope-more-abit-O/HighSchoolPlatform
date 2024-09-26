package com.demo.admissionportal.repository;


import com.demo.admissionportal.entity.ExamLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExamLocalRepository extends JpaRepository<ExamLocal, Integer> {
    ExamLocal findByName(String local);

    List<ExamLocal> findByIdIn(Set<Integer> examLocalIds);

    @Query("SELECT e.name FROM ExamLocal e WHERE e.id = :localId")
    Optional<String> findLocalNameById(Integer localId);
}
