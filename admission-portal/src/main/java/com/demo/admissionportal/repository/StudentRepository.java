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
     * @param username the username
     * @return the optional
     */
    Optional<Student> findByUsername(String username);

    /**
     * The constant findByEmail.
     *
     * @param email the email
     * @return the student
     */
    Student findByEmail(String email);

    /**
     * Find by phone student.
     *
     * @param phone the phone
     * @return the student
     */
    Student findByPhone(String phone);
}