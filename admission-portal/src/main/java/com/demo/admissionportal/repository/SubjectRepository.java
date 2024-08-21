package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.entity.Subject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Find by status list.
     *
     * @param status the status
     * @return the list
     */
    List<Subject> findByStatus(SubjectStatus status);

    /**
     * Find all page.
     *
     * @param name         the name
     * @param statusString the status string
     * @param pageable     the pageable
     * @return the page
     */
    @Query(value = "SELECT * FROM subject s WHERE " +
            "(:name IS NULL OR s.name LIKE %:#{#name}%) AND " +
            "(:statusString IS NULL OR s.status = :statusString) ", nativeQuery = true)
    Page<Subject> findAll(String name, String statusString, Pageable pageable);

    Optional<Subject> findByName(String name);

    List<Subject> findAllByStatus(SubjectStatus status);
}
