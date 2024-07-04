package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.CreateUniversityRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreateUniversityRequestRepository extends JpaRepository<CreateUniversityRequest, Integer> {

}
