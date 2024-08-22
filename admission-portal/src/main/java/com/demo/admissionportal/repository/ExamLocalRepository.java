package com.demo.admissionportal.repository;


import com.demo.admissionportal.ExamLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExamLocalRepository extends JpaRepository<ExamLocal, Integer> {
    ExamLocal findByName(String local);

    List<ExamLocal> findByIdIn(Set<Integer> examLocalIds);
}
