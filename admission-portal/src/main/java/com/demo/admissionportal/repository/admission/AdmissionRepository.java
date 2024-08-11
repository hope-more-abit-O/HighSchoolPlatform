package com.demo.admissionportal.repository.admission;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.entity.admission.Admission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {

    @Query(value = """
    SELECT *
    FROM admission
    WHERE (:id IS NULL OR id = :id)
      AND (:year IS NULL OR year = :year)
      AND (:source IS NULL OR source LIKE %:source%)
      AND (:universityId IS NULL OR university_id = :universityId)
      AND (:createTime IS NULL OR create_time = :createTime)
      AND (:createBy IS NULL OR create_by = :createBy)
      AND (:updateBy IS NULL OR update_by = :updateBy)
      AND (:updateTime IS NULL OR update_time = :updateTime)
      AND (:status IS NULL OR status = :status)
      AND (:scoreStatus IS NULL OR score_status = :scoreStatus)
    """, nativeQuery = true)
    Page<Admission> findAllBy(
            Pageable pageable,
            @Param("id") Integer id,
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
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE (:universityCode IS NULL OR LOWER(ui."code") = LOWER(:universityCode))
    AND (:year IS NULL OR year = :year)
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByYearAndUniversityCode(Pageable pageable ,Integer year, String universityCode);

    @Query(value = """
    SELECT TOP(:top) ad.*
    FROM admission ad
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE (:universityCode IS NULL OR LOWER(ui."code") = LOWER(:universityCode))
    AND (:year IS NULL OR year = :year)
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByYearAndUniversityCode(Integer year, String universityCode, Integer top);

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE (:universityCode IS NULL OR LOWER(ui."code") = LOWER(:universityCode))
    AND (:year IS NULL OR year = :year)
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    List<Admission> findAllByYearAndUniversityCode(Integer year, String universityCode);

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
""", nativeQuery = true)
    List<Admission> find(Pageable pageable);
}
