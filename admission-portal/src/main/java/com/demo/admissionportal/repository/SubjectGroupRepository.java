package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.SubjectGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Subject group repository.
 */
@Repository
public interface SubjectGroupRepository extends JpaRepository<SubjectGroup, Integer> {
    SubjectGroup findByName(String name);

    @Query(value = "SELECT DISTINCT sg.id, sg.name, sg.status, sg.create_by, sg.update_by, sg.create_time, sg.update_time FROM subject_group sg " +
            "LEFT JOIN subject_group_subject sgs ON sg.id = sgs.subject_group_id " +
            "LEFT JOIN subject s ON sgs.subject_id = s.id",
            countQuery = "SELECT COUNT(DISTINCT sg.id) FROM subject_group sg " +
                    "LEFT JOIN subject_group_subject sgs ON sg.id = sgs.subject_group_id " +
                    "LEFT JOIN subject s ON sgs.subject_id = s.id",
            nativeQuery = true)
    Page<SubjectGroup> findAll(Pageable pageable);




}
