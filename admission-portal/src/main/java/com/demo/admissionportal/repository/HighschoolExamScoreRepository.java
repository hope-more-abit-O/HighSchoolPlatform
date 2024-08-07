package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.HighschoolExamScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HighschoolExamScoreRepository extends JpaRepository<HighschoolExamScore, Integer> {

    @Query(value = "SELECT * FROM highschool_exam_score h WHERE " +
            "(:identificationNumber IS NULL OR h.identification_number = :identificationNumber) " +
            "AND h.year = 2024",
            nativeQuery = true)
    List<HighschoolExamScore> findAll(@Param("identificationNumber") Integer identificationNumber);

    @Query("SELECT COUNT(h) FROM HighschoolExamScore h WHERE h.identificationNumber = :identificationNumber AND h.year = 2024")
    int countByIdentificationNumber(@Param("identificationNumber") Integer identificationNumber);

    List<HighschoolExamScore> findByExaminer(String examiner);
    List<HighschoolExamScore> findByIdentificationNumber(Integer identificationNumber);
    List<HighschoolExamScore> findByIdentificationNumberAndYear(Integer identificationNumber, Integer year);
    @Query("SELECT h FROM HighschoolExamScore h WHERE h.local = :local")
    List<HighschoolExamScore> findByLocal(@Param("local") String local);


    @Query(value = "SELECT h.identification_number, h.subject_id, h.score, h.local FROM highschool_exam_score h " +
            "WHERE h.year = 2024 AND " +
            "(:local IS NULL OR h.local = :local) AND " +
            "h.subject_id IN (:subjectIds)", nativeQuery = true)
    List<Object[]> findScoresForSubjects(@Param("subjectIds") List<Integer> subjectIds, @Param("local") String local);

    @Query(value = "SELECT h.examiner, h.subject_id FROM highschool_exam_score h WHERE " +
            "h.year = 2024 AND (:local IS NULL OR h.local LIKE %:local%)", nativeQuery = true)
    List<Object[]> findExaminerAndSubjects(@Param("local") String local);

    @Query("SELECT s.local, s.score FROM HighschoolExamScore s WHERE s.subjectId = :subjectId AND s.local = :local")
    List<Object[]> findScoresBySubjectIdAndLocal(@Param("subjectId") Integer subjectId, @Param("local") String local);

    @Query("SELECT s.local, s.score FROM HighschoolExamScore s WHERE s.subjectId = :subjectId")
    List<Object[]> findScoresBySubjectId(@Param("subjectId") Integer subjectId);



}

