package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Integer> {

    Optional<Consultant> findByEmailOrUsername(String email, String username);

    Optional<Consultant> findByPhone(String phone);

    Optional<Consultant> findByEmail(String email);

    Optional<Consultant> findByUsername(String username);
}
