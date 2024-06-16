package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {

    Optional<University> findByUsernameOrName(String username, String name);

    List<University> findByUsernameOrEmailOrCode(String username, String email, String code);

    List<University> findByUsernameOrEmail(String username, String email);
}
