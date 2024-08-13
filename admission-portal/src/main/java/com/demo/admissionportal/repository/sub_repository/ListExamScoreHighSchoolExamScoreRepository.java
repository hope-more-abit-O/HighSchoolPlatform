package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.ListExamScoreHighSchoolExamScore;
import com.demo.admissionportal.entity.sub_entity.id.ListExamScoreHighSchoolExamScoreId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListExamScoreHighSchoolExamScoreRepository extends JpaRepository<ListExamScoreHighSchoolExamScore, ListExamScoreHighSchoolExamScoreId> {
    List<ListExamScoreHighSchoolExamScore> findAllByListExamScoreByYearId(Integer listExamScoreByYearId);
}
