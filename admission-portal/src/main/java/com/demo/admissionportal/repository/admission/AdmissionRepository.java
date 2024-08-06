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
            @Param("status") String status
    );

    @Query(value = """
    SELECT ad.*
    FROM admission ad
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE (:search IS NULL OR (LOWER(ui."code") LIKE CONCAT('%', :search, '%') OR LOWER(ui."name") LIKE CONCAT('%', :search, '%')))
    AND (:year IS NULL OR year = :year)
    AND (:status IS NULL OR ad.status = :status)
    """, nativeQuery = true)
    Page<Admission> findAllBy(
            Pageable pageable,
            @Param("year") Integer year,
            @Param("search") String search,
            @Param("status") String status
    );

    @Query(value = """
    SELECT TOP(1) ad.*
    FROM admission ad
    LEFT JOIN university_info ui ON ui.university_id = ad.university_id
    LEFT JOIN [user] usr ON usr.id = ad.university_id
    WHERE LOWER(ui."code") = LOWER(:universityCode)
    AND (year = :year)
    AND (ad.status = 'ACTIVE')
    """, nativeQuery = true)
    Optional<Admission> findByYearAndUniversityCode(Integer year, String universityCode);

    Optional<Admission> findFirstByUniversityIdAndStatusOrderByYearDesc(Integer universityId, AdmissionStatus status);
}
