package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Student repository.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    /**
     * Find by username optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Student> findByUsername(String email);
}
