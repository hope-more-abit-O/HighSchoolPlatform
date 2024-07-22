package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreateUniversityRequestRepository extends JpaRepository<CreateUniversityRequest, Integer> {

    Optional<CreateUniversityRequest> findFirstByUniversityUsername(String universityUsername);

    Optional<CreateUniversityRequest> findFirstByUniversityEmailAndStatusNot(String universityEmail, CreateUniversityRequestStatus status);
    Optional<CreateUniversityRequest> findFirstByUniversityCodeAndStatusNot(String universityCode, CreateUniversityRequestStatus status);
    Optional<CreateUniversityRequest> findFirstByUniversityUsernameAndStatusNot(String universityUsername, CreateUniversityRequestStatus status);

    Page<CreateUniversityRequest> findByCreateBy(Integer createBy, Pageable pageable);
}
