package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreateUniversityRequestRepository extends JpaRepository<CreateUniversityRequest, Integer> {

    Optional<CreateUniversityRequest> findFirstByUniversityUsername(String universityUsername);

    Optional<CreateUniversityRequest> findFirstByUniversityEmailAndStatusNot(String universityEmail, CreateUniversityRequestStatus status);
    Optional<CreateUniversityRequest> findFirstByUniversityCodeAndStatusNot(String universityCode, CreateUniversityRequestStatus status);
    Optional<CreateUniversityRequest> findFirstByUniversityUsernameAndStatusNot(String universityUsername, CreateUniversityRequestStatus status);

    Page<CreateUniversityRequest> findByCreateBy(Integer createBy, Pageable pageable);

    @Query(value = """
    SELECT cur.*
    FROM [create_university_request] cur
    INNER JOIN [user] usr ON usr.id = cur.create_by
    LEFT JOIN [staff_info] si ON si.staff_id = usr.id
    WHERE (:id IS NULL OR cur.id = :id)
        AND (:universityName IS NULL OR LOWER(cur.university_name) LIKE LOWER(CONCAT('%', :universityName, '%')))
        AND (:universityCode IS NULL OR LOWER(cur.university_code) LIKE LOWER(CONCAT('%', :universityCode, '%')))
        AND (:universityEmail IS NULL OR LOWER(cur.university_email) LIKE LOWER(CONCAT('%', :universityEmail, '%')))
        AND (:universityUsername IS NULL OR LOWER(cur.university_username) LIKE LOWER(CONCAT('%', :universityUsername, '%')))
        AND (cur.status IN (:status))
        AND (:createBy IS NULL OR cur.create_by = :createBy)
        AND (:confirmBy IS NULL OR cur.confirm_by = :confirmBy)
        AND (:createByName IS NULL OR LOWER(CONCAT(COALESCE(si.first_name, ''), ' ', COALESCE(si.middle_name, ''), ' ', COALESCE(si.last_name, ''))) LIKE LOWER(CONCAT('%', :createByName, '%')))
    """, nativeQuery = true)
    Page<CreateUniversityRequest> findAllBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("universityName") String universityName,
            @Param("universityCode") String universityCode,
            @Param("universityEmail") String universityEmail,
            @Param("universityUsername") String universityUsername,
            @Param("status") List<String> status,
            @Param("createBy") Integer createBy,
            @Param("createByName") String createByName,
            @Param("confirmBy") Integer confirmBy
    );

    @Query(value = """
    SELECT cur.*
    FROM [create_university_request] cur
    INNER JOIN [user] usr ON usr.id = cur.create_by
    LEFT JOIN [staff_info] si ON si.staff_id = usr.id
    WHERE (:id IS NULL OR cur.id = :id)
        AND (:universityName IS NULL OR LOWER(cur.university_name) LIKE LOWER(CONCAT('%', :universityName, '%')))
        AND (:universityCode IS NULL OR LOWER(cur.university_code) LIKE LOWER(CONCAT('%', :universityCode, '%')))
        AND (:universityEmail IS NULL OR LOWER(cur.university_email) LIKE LOWER(CONCAT('%', :universityEmail, '%')))
        AND (:universityUsername IS NULL OR LOWER(cur.university_username) LIKE LOWER(CONCAT('%', :universityUsername, '%')))
        AND (:createBy IS NULL OR cur.create_by = :createBy)
        AND (:confirmBy IS NULL OR cur.confirm_by = :confirmBy)
        AND (:createByName IS NULL OR LOWER(CONCAT(COALESCE(si.first_name, ''), ' ', COALESCE(si.middle_name, ''), ' ', COALESCE(si.last_name, ''))) LIKE LOWER(CONCAT('%', :createByName, '%')))
    """, nativeQuery = true)
    Page<CreateUniversityRequest> findAllBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("universityName") String universityName,
            @Param("universityCode") String universityCode,
            @Param("universityEmail") String universityEmail,
            @Param("universityUsername") String universityUsername,
            @Param("createBy") Integer createBy,
            @Param("createByName") String createByName,
            @Param("confirmBy") Integer confirmBy
    );
}
