package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.HighschoolExamScoreStatus;
import com.demo.admissionportal.dto.UniInfoDTO;
import com.demo.admissionportal.entity.ExamYear;
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
    List<HighschoolExamScore> findAll(@Param("identificationNumber") String identificationNumber, @Param("year") Integer year);

    @Query("SELECT COUNT(h) FROM HighschoolExamScore h " +
            "JOIN h.examYear ey " +
            "WHERE h.identificationNumber = :identificationNumber AND ey.year = :year")
    int countByIdentificationNumberAndExamYear(@Param("identificationNumber") String identificationNumber, @Param("year") Integer year);

    @Query(value = """
    SELECT h.*
    FROM highschool_exam_score h 
    WHERE h.exam_year_id = :examYearId
    AND h.identification_number = :identificationNumber
""", nativeQuery = true)
    List<HighschoolExamScore> findByIdentificationNumberAndExamYear_Id(@Param("identificationNumber") String identificationNumber, @Param("examYearId") Integer examYearId);

    @Query(value = """
    SELECT COUNT(h.identification_number)
    FROM highschool_exam_score h
    WHERE h.exam_year_id = :examYearId
    AND h.identification_number = :identificationNumber
""", nativeQuery = true)
    long countByIdentificationNumberAndExamYearId(@Param("identificationNumber") String identificationNumber, @Param("examYearId") Integer examYearId);


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
    List<String> findTop100StudentsBySubject(@Param("subjectName") String subjectName, @Param("local") Integer localId);

    @Query(value = "SELECT TOP 100 h.identification_number " +
            "FROM highschool_exam_score h " +
            "WHERE h.exam_year_id = 4 AND " +
            "(:local IS NULL OR h.exam_local_id = :local) AND " +
            "h.subject_id IN (:subjectIds) " +
            "GROUP BY h.identification_number " +
            "ORDER BY SUM(h.score) DESC", nativeQuery = true)
    List<String> findTop100StudentsBySubjects(@Param("subjectIds") List<Integer> subjectIds, @Param("local") Integer localId);


    @Query(value = "SELECT h.* " +
            "FROM highschool_exam_score h " +
            "WHERE h.identification_number IN :identificationNumbers " +
            "ORDER BY h.identification_number, h.subject_id", nativeQuery = true)
    List<HighschoolExamScore> findScoresByIdentificationNumbers(@Param("identificationNumbers") List<String> identificationNumbers);


    @Query("SELECT h FROM HighschoolExamScore h WHERE h.examYear = :examYear AND h.status = :status")
    List<HighschoolExamScore> findAllByExamYearAndStatus(@Param("examYear") ExamYear examYear, @Param("status") HighschoolExamScoreStatus status);

    //    Page<HighschoolExamScore> findByYear(Integer examYearId, Pageable pageable);
    @Query("SELECT h FROM HighschoolExamScore h WHERE h.examYear.year = :year AND h.examLocal.name = :local AND h.status = :status")
    List<HighschoolExamScore> findAllByYearAndLocalAndStatus(@Param("year") Integer year, @Param("local") String local, @Param("status") HighschoolExamScoreStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE HighschoolExamScore h SET h.status = :inactiveStatus WHERE h.examYear.id != :examYearId")
    int deactivateOtherHighSchoolExamScores(@Param("examYearId") Integer examYearId,
                                            @Param("inactiveStatus") HighschoolExamScoreStatus inactiveStatus);


    @Modifying
    @Transactional
    @Query("UPDATE HighschoolExamScore h SET h.status = :status WHERE h.examYear = :examYear AND h.status = :oldStatus")
    int updateStatusByExamYear(@Param("status") HighschoolExamScoreStatus status, @Param("examYear") ExamYear examYear, @Param("oldStatus") HighschoolExamScoreStatus oldStatus);

//    @Query("SELECT h FROM HighschoolExamScore h WHERE h.identificationNumber IN :identificationNumbers AND h.year = :year")
//    List<HighschoolExamScore> findByIdentificationNumberAndYearIn(@Param("identificationNumbers") List<Integer> identificationNumbers, @Param("year") Integer year);

    List<HighschoolExamScore> findByIdentificationNumberIn(Set<String> identificationNumbers);

    List<HighschoolExamScore> findByIdentificationNumberAndSubjectIdIn(String identificationNumber, List<Integer> subjectIds);

    boolean existsByIdentificationNumber(String identificationNumber);

    boolean existsByIdentificationNumberAndExamLocalId(String identificationNumber, Integer examLocalId);

    @Query(value = "SELECT h.identification_number, h.subject_id, h.score, h.exam_local_id FROM highschool_exam_score h " +
            "WHERE h.exam_year_id = 4 AND " +
            "h.subject_id IN (:subjectIds) AND h.status = 'ACTIVE'", nativeQuery = true)
    List<Object[]> findScoresForSubjectsOnly(List<Integer> subjectIds);

    @Query("SELECT hes FROM HighschoolExamScore hes " +
            "JOIN hes.examYear ey " +
            "WHERE ey.year = :year")
    Page<HighschoolExamScore> findByExamYear(@Param("year") Integer year, Pageable pageable);

    @Query("""
             SELECT h
             FROM HighschoolExamScore h
             WHERE h.identificationNumber = :identificationNumber
             AND h.score IS NOT NULL
""")
    List<HighschoolExamScore> findByIdentificationNumberAndScoreIsNotNull(String identificationNumber);



    @Query(value = """
        SELECT DISTINCT a.university_id, u.name, u.code
        FROM admission_training_program atp
            INNER JOIN admission_training_program_method atpm ON atp.id = atpm.admission_training_program_id
            INNER JOIN admission_method am ON atpm.admission_method_id = am.id
            INNER JOIN admission a ON atp.admission_id = a.id
            INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
            INNER JOIN university_info u ON a.university_id = u.university_id
            INNER JOIN (
                SELECT a.university_id, atp.major_id, atpsg.subject_group_id           
                FROM admission_training_program_method atpm
                INNER JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
                INNER JOIN admission a ON atp.admission_id = a.id
                INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
                WHERE a.status = 'ACTIVE' AND a.year = 2024
                INTERSECT
                SELECT a.university_id, atp.major_id, atpsg.subject_group_id           
                FROM admission_training_program_method atpm
                INNER JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
                INNER JOIN admission a ON atp.admission_id = a.id
                INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
                WHERE a.status = 'ACTIVE' AND a.year = 2023 AND atpm.addmission_score IS NOT NULL
            ) ir ON atp.major_id = ir.major_id
                      AND a.university_id = ir.university_id
        WHERE a.status = 'ACTIVE'
          AND a.confirm_status = 'CONFIRMED'
          AND a.year IN (2023, 2024)
        """, nativeQuery = true)
    List<Object[]> findDistinctUniversityIds();


    @Query(value = """
          SELECT DISTINCT atp.major_id, m.name, m.code
          FROM admission_training_program atp
                INNER JOIN admission_training_program_method atpm ON atp.id = atpm.admission_training_program_id
                INNER JOIN admission_method am ON atpm.admission_method_id = am.id
                INNER JOIN admission a ON atp.admission_id = a.id
                INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
                INNER JOIN major m ON atp.major_id = m.id
                INNER JOIN (
                    SELECT a.university_id, atp.major_id, atpsg.subject_group_id
                    FROM admission_training_program_method atpm
                        INNER JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
                        INNER JOIN admission a ON atp.admission_id = a.id
                        INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
                    WHERE a.status = 'ACTIVE'
                      AND a.university_id = :universityId
                      AND a.year = 2024
                    INTERSECT
                    SELECT a.university_id, atp.major_id, atpsg.subject_group_id
                    FROM admission_training_program_method atpm
                        INNER JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
                        INNER JOIN admission a ON atp.admission_id = a.id
                        INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
                    WHERE a.status = 'ACTIVE'
                      AND a.university_id = :universityId
                      AND a.year = 2023
                      AND atpm.addmission_score IS NOT NULL
                ) ir ON atp.major_id = ir.major_id
                AND a.university_id = ir.university_id
        WHERE a.status = 'ACTIVE'
          AND a.confirm_status = 'CONFIRMED'
          AND a.year IN (2023, 2024)
        ORDER BY atp.major_id ASC
""", nativeQuery = true)
    List<Object[]> findDistinctMajorByUniversityId(@Param("universityId") Integer universityId);


    @Query(value = """
SELECT DISTINCT sgs.subject_group_id, sg.name, s.id, s.name
FROM admission_training_program atp
    INNER JOIN admission_training_program_method atpm ON atp.id = atpm.admission_training_program_id
    INNER JOIN admission_method am ON atpm.admission_method_id = am.id
    INNER JOIN admission a ON atp.admission_id = a.id
    INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
    INNER JOIN subject_group sg ON atpsg.subject_group_id = sg.id
    INNER JOIN subject_group_subject sgs ON atpsg.subject_group_id = sgs.subject_group_id
    INNER JOIN subject s ON sgs.subject_id = s.id
WHERE a.status = 'ACTIVE'
  AND a.confirm_status = 'CONFIRMED'
  AND a.year IN (2023, 2024)
  AND atpsg.subject_group_id IN (
        SELECT DISTINCT atpsg.subject_group_id
        FROM admission_training_program_method atpm
            INNER JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
            INNER JOIN admission a ON atp.admission_id = a.id
            INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
        WHERE a.status = 'ACTIVE'
          AND a.year = 2024
          AND a.university_id = :universityId
          AND atp.major_id = :majorId
        INTERSECT
        SELECT DISTINCT atpsg.subject_group_id
        FROM admission_training_program_method atpm
            INNER JOIN admission_training_program atp ON atpm.admission_training_program_id = atp.id
            INNER JOIN admission a ON atp.admission_id = a.id
            INNER JOIN admission_training_program_subject_group atpsg ON atp.id = atpsg.admission_training_program_id
        WHERE a.status = 'ACTIVE'
          AND a.year = 2023
          AND atpm.addmission_score IS NOT NULL
          AND a.university_id = :universityId
          AND atp.major_id = :majorId
    )
ORDER BY sgs.subject_group_id ASC;

""", nativeQuery = true)
    List<Object[]> findSubjectGroupByUniversityIdAndMajorId(@Param("universityId") Integer universityId, @Param("majorId") Integer majorId);
}