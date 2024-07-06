package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Subject repository.
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    /**
     * Find subject by name subject.
     *
     * @param name the name
     * @return the subject
     */
    Subject findSubjectByName(String name);
    List<Subject> findByStatus(SubjectStatus status);
}
