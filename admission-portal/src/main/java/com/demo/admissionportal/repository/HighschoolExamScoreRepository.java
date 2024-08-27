package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.HighschoolExamScoreStatus;
import com.demo.admissionportal.entity.HighschoolExamScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface HighschoolExamScoreRepository extends JpaRepository<HighschoolExamScore, Integer> {

    @Query("SELECT h FROM HighschoolExamScore h " +
            "JOIN h.examYear ey " +
            "WHERE (:identificationNumber IS NULL OR h.identificationNumber = :identificationNumber) " +
            "AND (:year IS NULL OR ey.year = :year)")
    List<HighschoolExamScore> findAll(@Param("identificationNumber") Integer identificationNumber, @Param("year") Integer year);

    @Query("SELECT COUNT(h) FROM HighschoolExamScore h " +
            "JOIN h.examYear ey " +
            "WHERE h.identificationNumber = :identificationNumber AND ey.year = :year")
    int countByIdentificationNumberAndExamYear(@Param("identificationNumber") Integer identificationNumber, @Param("year") Integer year);

    List<HighschoolExamScore> findByIdentificationNumber(Integer identificationNumber);
    List<HighschoolExamScore> findByIdentificationNumberAndExamYear_Id(Integer identificationNumber, Integer examYearId);


    @Query(value = "SELECT h.identification_number, h.subject_id, h.score, h.exam_local_id FROM highschool_exam_score h " +
            "WHERE h.exam_year_id = 4 AND " +
            "(:local IS NULL OR h.exam_local_id = :local) AND " +
            "h.subject_id IN (:subjectIds) AND h.status = 'ACTIVE'", nativeQuery = true)
    List<Object[]> findScoresForSubjects(@Param("subjectIds") List<Integer> subjectIds, @Param("local") Integer local);


    Page<HighschoolExamScore> findByExamYearId(Integer examYearId, Pageable pageable);

    @Query("SELECT s.examLocal.name, s.score FROM HighschoolExamScore s WHERE s.subjectId = :subjectId AND s.examLocal.name = :local AND s.status = 'ACTIVE'")
    List<Object[]> findScoresBySubjectIdAndLocal(@Param("subjectId") Integer subjectId, @Param("local") String local);

    @Query("SELECT s.examLocal.name, s.score FROM HighschoolExamScore s WHERE s.subjectId = :subjectId AND s.status = 'ACTIVE'")
    List<Object[]> findScoresBySubjectId(@Param("subjectId") Integer subjectId);

    @Query(value = "SELECT TOP (100) h.identification_number " +
            "FROM highschool_exam_score h " +
            "INNER JOIN subject s ON h.subject_id = s.id " +
            "WHERE s.name = :subjectName AND h.exam_year_id = 4 " +
            "AND (:local IS NULL OR h.exam_local_id = :local) AND h.status = 'ACTIVE'" +
            "ORDER BY h.score DESC ", nativeQuery = true)
    List<Integer> findTop100StudentsBySubject(@Param("subjectName") String subjectName, @Param("local") Integer localId);

    @Query(value = "SELECT TOP 100 h.identification_number " +
            "FROM highschool_exam_score h " +
            "WHERE h.exam_year_id = 4 AND " +
            "(:local IS NULL OR h.exam_local_id = :local) AND " +
            "h.subject_id IN (:subjectIds) " +
            "GROUP BY h.identification_number " +
            "ORDER BY SUM(h.score) DESC", nativeQuery = true)
    List<Integer> findTop100StudentsBySubjects(@Param("subjectIds") List<Integer> subjectIds, @Param("local") Integer localId);


    @Query(value = "SELECT h.* " +
            "FROM highschool_exam_score h " +
            "WHERE h.identification_number IN :identificationNumbers " +
            "ORDER BY h.identification_number, h.subject_id", nativeQuery = true)
    List<HighschoolExamScore> findScoresByIdentificationNumbers(@Param("identificationNumbers") List<Integer> identificationNumbers);


    @Query("SELECT h FROM HighschoolExamScore h WHERE h.examYear = :examYear AND h.status = :status")
    List<HighschoolExamScore> findAllByExamYearAndStatus(@Param("examYear") Integer examYear, @Param("status") HighschoolExamScoreStatus status);

//    Page<HighschoolExamScore> findByYear(Integer examYearId, Pageable pageable);
@Query("SELECT h FROM HighschoolExamScore h WHERE h.examYear.year = :year AND h.examLocal.name = :local AND h.status = :status")
List<HighschoolExamScore> findAllByYearAndLocalAndStatus(@Param("year") Integer year, @Param("local") String local, @Param("status") HighschoolExamScoreStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE HighschoolExamScore h SET h.status = :status WHERE h.examYear = :examYear AND h.status = :oldStatus")
    int updateStatusByExamYear(@Param("status") HighschoolExamScoreStatus status, @Param("examYear") Integer examYear, @Param("oldStatus") HighschoolExamScoreStatus oldStatus);

//    @Query("SELECT h FROM HighschoolExamScore h WHERE h.identificationNumber IN :identificationNumbers AND h.year = :year")
//    List<HighschoolExamScore> findByIdentificationNumberAndYearIn(@Param("identificationNumbers") List<Integer> identificationNumbers, @Param("year") Integer year);

    List<HighschoolExamScore> findByIdentificationNumberIn(Set<Integer> identificationNumbers);

    List<HighschoolExamScore> findByIdentificationNumberAndSubjectIdIn(Integer identificationNumber, List<Integer> subjectIds);

    boolean existsByIdentificationNumber(Integer identificationNumber);

    boolean existsByIdentificationNumberAndExamLocalId(Integer identificationNumber, Integer examLocalId);

    @Query(value = "SELECT h.identification_number, h.subject_id, h.score, h.exam_local_id FROM highschool_exam_score h " +
            "WHERE h.exam_year_id = 4 AND " +
            "h.subject_id IN (:subjectIds) AND h.status = 'ACTIVE'", nativeQuery = true)
    List<Object[]> findScoresForSubjectsOnly(List<Integer> subjectIds);

    @Query("SELECT hes FROM HighschoolExamScore hes " +
            "JOIN hes.examYear ey " +
            "WHERE ey.year = :year")
    Page<HighschoolExamScore> findByExamYear(@Param("year") Integer year, Pageable pageable);

}





