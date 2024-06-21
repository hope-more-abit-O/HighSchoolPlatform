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

    Optional<Student> findFirstByUsernameOrEmail(String username, String email);

    Optional<Student> findFirstByPhone(String phone);

    Optional<Student> findFirstByPhoneAndIdNot(String email, Integer id);

    Optional<Student> findFirstByEmailAndIdNot(String email, Integer id);

    Optional<Student> findFirstByEmail(String email);

    Optional<Student> findFirstByUsernameAndIdNot(String username, Integer id);

    Optional<Student> findFirstByUsername(String username);
}
