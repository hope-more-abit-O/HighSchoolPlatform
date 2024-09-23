package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.constants.AdmissionConfirmStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.entity.admission.Admission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {

    @Query(value = """
    SELECT a.*
    FROM admission a
    INNER JOIN university_info ui ON a.university_id = ui.university_id
    WHERE (:id IS NULL OR a.id = :id)
      AND (:year IS NULL OR a.year = :year)
      AND (:staffId IS NULL OR ui.staff_id = :staffId)
      AND (:source IS NULL OR a.source LIKE %:source%)
      AND (:universityId IS NULL OR a.university_id = :universityId)
      AND (:createTime IS NULL OR a.create_time = :createTime)
      AND (:createBy IS NULL OR a.create_by = :createBy)
      AND (:updateBy IS NULL OR a.update_by = :updateBy)
      AND (:updateTime IS NULL OR a.update_time = :updateTime)
      AND (:status IS NULL OR a.status = :status)
      AND (:scoreStatus IS NULL OR a.score_status = :scoreStatus)
      AND (a.confirm_status IN :confirmStatus)
    """,
            countQuery = """
    SELECT COUNT(a.id)
    FROM admission a
    INNER JOIN university_info ui ON a.university_id = ui.university_id
    WHERE (:id IS NULL OR a.id = :id)
      AND (:year IS NULL OR a.year = :year)
      AND (:staffId IS NULL OR ui.staff_id = :staffId)
      AND (:source IS NULL OR a.source LIKE %:source%)
      AND (:universityId IS NULL OR a.university_id = :universityId)
      AND (:createTime IS NULL OR a.create_time = :createTime)
      AND (:createBy IS NULL OR a.create_by = :createBy)
      AND (:updateBy IS NULL OR a.update_by = :updateBy)
      AND (:updateTime IS NULL OR a.update_time = :updateTime)
      AND (:status IS NULL OR a.status = :status)
      AND (:scoreStatus IS NULL OR a.score_status = :scoreStatus)
      AND (a.confirm_status IN :confirmStatus)
    """, nativeQuery = true)
    Page<Admission> findAllBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("staffId") Integer staffId,
            @Param("year") Integer year,
            @Param("source") String source,
            @Param("universityId") Integer universityId,
            @Param("createTime") Date createTime,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy,
            @Param("updateTime") Date updateTime,
            @Param("status") String status,
            @Param("scoreStatus") String scoreStatus,
            @Param("confirmStatus") List<String> confirmStatus
    );
    @Query(value = """
    SELECT a.*
    FROM admission a
    INNER JOIN university_info ui ON a.university_id = ui.university_id
    WHERE (:id IS NULL OR a.id = :id)
      AND (:year IS NULL OR a.year = :year)
      AND (:staffId IS NULL OR ui.staff_id = :staffId)
      AND (:source IS NULL OR a.source LIKE %:source%)
      AND (:universityId IS NULL OR a.university_id = :universityId)
      AND (:createTime IS NULL OR a.create_time = :createTime)
      AND (:createBy IS NULL OR a.create_by = :createBy)
      AND (:updateBy IS NULL OR a.update_by = :updateBy)
      AND (:updateTime IS NULL OR a.update_time = :updateTime)
      AND (:status IS NULL OR a.status = :status)
      AND (:scoreStatus IS NULL OR a.score_status = :scoreStatus)
    """,
            countQuery = """
    SELECT COUNT(a.id)
    FROM admission a
    INNER JOIN university_info ui ON a.university_id = ui.university_id
    WHERE (:id IS NULL OR a.id = :id)
      AND (:year IS NULL OR a.year = :year)
      AND (:staffId IS NULL OR ui.staff_id = :staffId)
      AND (:source IS NULL OR a.source LIKE %:source%)
      AND (:universityId IS NULL OR a.university_id = :universityId)
      AND (:createTime IS NULL OR a.create_time = :createTime)
      AND (:createBy IS NULL OR a.create_by = :createBy)
      AND (:updateBy IS NULL OR a.update_by = :updateBy)
      AND (:updateTime IS NULL OR a.update_time = :updateTime)
      AND (:status IS NULL OR a.status = :status)
      AND (:scoreStatus IS NULL OR a.score_status = :scoreStatus)
    """, nativeQuery = true)
    Page<Admission> findAllBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("staffId") Integer staffId,
            @Param("year") Integer year,
            @Param("source") String source,
            @Param("universityId") Integer universityId,
            @Param("createTime") Date createTime,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy,
            @Param("updateTime") Date updateTime,
            @Param("status") String status,
            @Param("scoreStatus") String scoreStatus
    );


    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ad.university_id = ui.university_id
    WHERE (ad.university_id IN (:universityId))
    AND (ad.year IN (:year))
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByListYearAndListUniversityId(Pageable pageable , List<Integer> year, List<String> universityId);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ad.university_id = ui.university_id
    WHERE (ui.code IN (:universityId))
    AND (ad.year IN (:year))
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByListYearAndListUniversityCode(Pageable pageable , List<Integer> year, List<String> universityId);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ad.university_id = ui.university_id
    WHERE (ui.code IN (:universityId))
    AND (ad.year IN (:year))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
    """,countQuery = """
    SELECT count(ad.id)
    FROM admission ad
    INNER JOIN university_info ui on ad.university_id = ui.university_id
    WHERE (ui.code IN (:universityId))
    AND (ad.year IN (:year))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> findAllByListYearAndListUniversityCodeV2(Pageable pageable , List<Integer> year, List<String> universityId, Integer staffId);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ad.university_id = ui.university_id
    WHERE (ui.code IN (:universityId))
    AND (ad.year IN (:year))
    AND (ad.status IN (:status))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
    """,countQuery = """
    SELECT count(ad.id)
    FROM admission ad
    INNER JOIN university_info ui on ad.university_id = ui.university_id
    WHERE (ui.code IN (:universityId))
    AND (ad.year IN (:year))
    AND (ad.status IN (:status))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> findAllByListYearAndListUniversityCodeAndListStatus(Pageable pageable , List<Integer> year, List<String> universityId, List<String> status, Integer staffId);


    @Query(value = """
    SELECT ad.*
    FROM admission ad
    WHERE (ad.year IN (:year))
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByListYear(Pageable pageable ,List<Integer> year);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ad.year IN (:year))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
    """, countQuery = """
    SELECT count(ad.id)
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ad.year IN (:year))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> findAllByListYearV2(Pageable pageable ,List<Integer> year, Integer staffId);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ad.year IN (:year))
    AND (ad.status IN (:status))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
    """,countQuery = """
    SELECT count(ad.id)
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ad.year IN (:year))
    AND (ad.status IN (:status))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> findAllByListYearAndListStatus(Pageable pageable, List<Integer> year, List<String> status, Integer staffId);


    @Query(value = """
    SELECT ad.*
    FROM admission ad
    WHERE (ad.university_id IN (:universityId))
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByListUniversityId(Pageable pageable , List<Integer> universityId);


    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ui.code in (:universityCode))
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByListUniversityCode(Pageable pageable , List<String> universityCode);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ui.code in (:universityCode))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
    """, countQuery = """
    SELECT count(ad.id)
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ui.code in (:universityCode))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> findAllByListUniversityCodeV2(Pageable pageable , List<String> universityCode, Integer staffId);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ui.code in (:universityCode))
    AND (ad.status in (:status))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
    """,countQuery = """
    SELECT count(ad.id)
    FROM admission ad
    INNER JOIN university_info ui on ui.university_id = ad.university_id
    WHERE (ui.code in (:universityCode))
    AND (ad.status in (:status))
    AND (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> findAllByListUniversityCodeAndListStatus(Pageable pageable , List<String> universityCode, List<String> status, Integer staffId);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    WHERE (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllActiveWithPageable(Pageable pageable);

    @Query(value = """
    SELECT TOP(:top) ad.*
    FROM admission ad
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE (:universityCode IS NULL OR LOWER(ui."code") = LOWER(:universityCode))
    AND (:year IS NULL OR year = :year)
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByListYearAndListUniversityId(Integer year, String universityCode, Integer top);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE (:universityCode IS NULL OR LOWER(ui."code") = LOWER(:universityCode))
    AND (:year IS NULL OR year = :year)
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByListYearAndListUniversityId(Integer year, String universityCode);

    @Query(value = """
    SELECT TOP(1) ad.*
    FROM admission ad
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE (:universityCode IS NULL OR LOWER(ui."code") = LOWER(:universityCode))
    AND (:year IS NULL OR year = :year)
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    Optional<Admission> findByYearAndUniversityCode(Integer year, String universityCode);

    Optional<Admission> findFirstByUniversityIdAndAdmissionStatusOrderByYearDesc(Integer universityId, AdmissionStatus status);

    @Query(value = """
SELECT * 
FROM admission ad
WHERE ad.year = :year
""", nativeQuery = true)
    List<Admission> findByYear(Pageable pageable,Integer year);

    @Query(value = """
select a.*
from admission a
inner join university_info ui on ui.university_id = a.university_id
where ui.code = :universityCode
""", nativeQuery = true)
    List<Admission> findByUniversityCode(Pageable pageable,String universityCode);

    @Query(value = """
select a.*
from admission a
inner join university_info ui on ui.university_id = a.university_id
where (:staffId IS NULL OR ui.staff_id = :staffId)
""", countQuery = """
select count(a.id)
from admission a
inner join university_info ui on ui.university_id = a.university_id
where (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> find(Pageable pageable, Integer staffId);

    @Query(value = """
select a.*
from admission a
inner join university_info ui on ui.university_id = a.university_id
where a.status in (:status)
and (:staffId IS NULL OR ui.staff_id = :staffId)
""" ,countQuery = """
select count(a.id)
from admission a
inner join university_info ui on ui.university_id = a.university_id
where a.status in (:status)
and (:staffId IS NULL OR ui.staff_id = :staffId)
""", nativeQuery = true)
    Page<Admission> findWithPageableAndListStatus(Pageable pageable, List<String> status, Integer staffId);

    Optional<Admission> findFirstByUniversityIdAndAdmissionStatus(Integer universityId, AdmissionStatus admissionStatus);

    Optional<Admission> findFirstByUniversityIdAndAdmissionStatusOrderByYearAsc(Integer universityId, AdmissionStatus admissionStatus);

    Optional<Admission> findByUniversityIdAndYearAndAdmissionStatus(Integer universityId, Integer year, AdmissionStatus admissionStatus);

    Optional<Admission> findFirstByUniversityIdAndAdmissionStatusAndYearOrderByYearDesc(Integer universityId, AdmissionStatus admissionStatus, Integer year);

    Optional<Admission> findFirstByUniversityIdAndAdmissionStatusAndYearLessThanEqualOrderByYearDesc(Integer universityId, AdmissionStatus admissionStatus, Integer year);


    @Query(value = """
SELECT *
FROM admission
WHERE university_id IN (:universityIds)
AND year = :year
AND status = :admissionStatus
""",nativeQuery = true)
    List<Admission> findByUniversityIdAndYearAndStatus(List<Integer> universityIds, int year, String admissionStatus);


    @Query("SELECT a FROM Admission a WHERE a.universityId = :universityId AND a.year = :year")
    List<Admission> findByUniversityAndYear(@Param("universityId") Integer universityId, @Param("year") Integer year);

    List<Admission> findByUniversityIdInAndYearAndAdmissionStatus(List<Integer> universityIds, Integer year, AdmissionStatus admissionStatus);

    Optional<Admission> findByUniversityIdAndYearAndConfirmStatus(Integer universityId, Integer year, AdmissionConfirmStatus confirmStatus);

    @Query(value = """
SELECT *
FROM admission
WHERE university_id = :universityId
AND year = :year
AND status = :admissionStatus
""", nativeQuery = true)
   List<Admission> findByUniversityIdAndYearAndAdmissionStatusV2(Integer universityId, Integer year, String admissionStatus);
}