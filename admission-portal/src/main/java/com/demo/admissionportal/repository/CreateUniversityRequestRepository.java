package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreateUniversityRequestRepository extends JpaRepository<CreateUniversityRequest, Integer> {

    Optional<CreateUniversityRequest> findFirstByUniversityUsername(String universityUsername);

    Optional<CreateUniversityRequest> findFirstByUniversityEmailAndStatusNot(String universityEmail, CreateUniversityRequestStatus status);
    Optional<CreateUniversityRequest> findFirstByUniversityCodeAndStatusNot(String universityCode, CreateUniversityRequestStatus status);
    Optional<CreateUniversityRequest> findFirstByUniversityUsernameAndStatusNot(String universityUsername, CreateUniversityRequestStatus status);

    Page<CreateUniversityRequest> findByCreateBy(Integer createBy, Pageable pageable);

    @Query(value = """
    SELECT *
    FROM create_university_request
    WHERE (:id IS NULL OR id = :id)
      AND (:universityName IS NULL OR university_name LIKE %:universityName%)
      AND (:universityCode IS NULL OR university_code LIKE %:universityCode%)
      AND (:universityEmail IS NULL OR university_email LIKE %:universityEmail%)
      AND (:universityUsername IS NULL OR university_username LIKE %:universityUsername%)
      AND (:status IS NULL OR status = :status)
      AND (:createBy IS NULL OR create_by = :createBy)
      AND (:confirmBy IS NULL OR confirm_by = :confirmBy)
    """, nativeQuery = true)
    Page<CreateUniversityRequest> findAllBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("universityName") String universityName,
            @Param("universityCode") String universityCode,
            @Param("universityEmail") String universityEmail,
            @Param("universityUsername") String universityUsername,
            @Param("status") String status,
            @Param("createBy") Integer createBy,
            @Param("confirmBy") Integer confirmBy
    );
}
