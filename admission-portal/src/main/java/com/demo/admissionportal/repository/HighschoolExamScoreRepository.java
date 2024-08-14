package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.HighschoolExamScoreStatus;
import com.demo.admissionportal.entity.HighschoolExamScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface HighschoolExamScoreRepository extends JpaRepository<HighschoolExamScore, Integer> {

    @Query(value = "SELECT * FROM highschool_exam_score h WHERE " +
            "(:identificationNumber IS NULL OR h.identification_number = :identificationNumber) " +
            "AND (:year IS NULL OR h.year = :year)",
            nativeQuery = true)
    List<HighschoolExamScore> findAll(@Param("identificationNumber") Integer identificationNumber, @Param("year") Integer year);


    @Query("SELECT COUNT(h) FROM HighschoolExamScore h WHERE h.identificationNumber = :identificationNumber AND h.year = :year")
    int countByIdentificationNumberAndYear(@Param("identificationNumber") Integer identificationNumber, @Param("year") Integer year);


    List<HighschoolExamScore> findByExaminer(String examiner);
    List<HighschoolExamScore> findByIdentificationNumber(Integer identificationNumber);
    List<HighschoolExamScore> findByIdentificationNumberAndYear(Integer identificationNumber, Integer year);
    @Query("SELECT h FROM HighschoolExamScore h WHERE h.local = :local")
    List<HighschoolExamScore> findByLocal(@Param("local") String local);


    @Query(value = "SELECT h.identification_number, h.subject_id, h.score, h.local FROM highschool_exam_score h " +
            "WHERE h.year = 2024 AND " +
            "(:local IS NULL OR h.local = :local) AND " +
            "h.subject_id IN (:subjectIds) AND h.status = 'ACTIVE'", nativeQuery = true)
    List<Object[]> findScoresForSubjects(@Param("subjectIds") List<Integer> subjectIds, @Param("local") String local);

    @Query(value = "SELECT h.examiner, h.subject_id FROM highschool_exam_score h WHERE " +
            "h.year = 2024 AND (:local IS NULL OR h.local LIKE %:local%) AND h.status = 'ACTIVE'", nativeQuery = true)
    List<Object[]> findExaminerAndSubjects(@Param("local") String local);

    @Query("SELECT s.local, s.score FROM HighschoolExamScore s WHERE s.subjectId = :subjectId AND s.local = :local AND s.status = 'ACTIVE'")
    List<Object[]> findScoresBySubjectIdAndLocal(@Param("subjectId") Integer subjectId, @Param("local") String local);

    @Query("SELECT s.local, s.score FROM HighschoolExamScore s WHERE s.subjectId = :subjectId AND s.status = 'ACTIVE'")
    List<Object[]> findScoresBySubjectId(@Param("subjectId") Integer subjectId);

    @Query(value = "SELECT TOP (100) h.identification_number " +
            "FROM highschool_exam_score h " +
            "INNER JOIN subject s ON h.subject_id = s.id " +
            "WHERE s.name = :subjectName AND h.year = 2024 " +
            "AND (:local IS NULL OR h.local = :local) AND h.status = 'ACTIVE'" +
            "ORDER BY h.score DESC ", nativeQuery = true)
    List<Integer> findTop100StudentsBySubject(@Param("subjectName") String subjectName, @Param("local") String local);

    @Query(value = "SELECT TOP 100 h.identification_number " +
            "FROM highschool_exam_score h " +
            "WHERE h.year = 2024 AND " +
            "(:local IS NULL OR h.local = :local) AND " +
            "h.subject_id IN (:subjectIds) " +
            "GROUP BY h.identification_number " +
            "ORDER BY SUM(h.score) DESC", nativeQuery = true)
    List<Integer> findTop100StudentsBySubjects(@Param("subjectIds") List<Integer> subjectIds, @Param("local") String local);


    @Query(value = "SELECT h.* " +
            "FROM highschool_exam_score h " +
            "WHERE h.identification_number IN :identificationNumbers " +
            "ORDER BY h.identification_number, h.subject_id", nativeQuery = true)
    List<HighschoolExamScore> findScoresByIdentificationNumbers(@Param("identificationNumbers") List<Integer> identificationNumbers);


    List<HighschoolExamScore> findAllByStatus(HighschoolExamScoreStatus status);

    @Query("SELECT h FROM HighschoolExamScore h WHERE h.year = :year AND h.status = :status")
    List<HighschoolExamScore> findAllByYearAndStatus(@Param("year") Integer year, @Param("status") HighschoolExamScoreStatus status);

    Page<HighschoolExamScore> findByYear(Integer year, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE HighschoolExamScore h SET h.status = :status WHERE h.year = :year AND h.status = :oldStatus")
    int updateStatusByYear(@Param("status") HighschoolExamScoreStatus status, @Param("year") Integer year, @Param("oldStatus") HighschoolExamScoreStatus oldStatus);

    @Query("SELECT h FROM HighschoolExamScore h WHERE h.identificationNumber IN :identificationNumbers AND h.year = :year")
    List<HighschoolExamScore> findByIdentificationNumberAndYearIn(@Param("identificationNumbers") List<Integer> identificationNumbers, @Param("year") Integer year);

    List<HighschoolExamScore> findByIdentificationNumberIn(Set<Integer> identificationNumbers);
}





