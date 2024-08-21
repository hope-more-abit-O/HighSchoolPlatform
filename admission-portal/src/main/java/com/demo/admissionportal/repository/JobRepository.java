package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Job;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * The interface Job repository.
 */
public interface JobRepository extends JpaRepository<Job, Integer> {
    /**
     * Find jobs page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT * " +
            "FROM job j " +
            "WHERE j.status = N'ACTIVE'", nativeQuery = true)
    Page<Job> findJobs(Pageable pageable);

    /**
     * Find job by content job.
     *
     * @param names the names
     * @return the job
     */
    @Query("SELECT j FROM Job j WHERE j.name IN :names")
    List<Job> findJobsByNameIn(@Param("names") Set<String> names);

    /**
     * Find jobs by id in list.
     *
     * @param jobIds the job ids
     * @return the list
     */
    @Query("SELECT j FROM Job j WHERE j.id IN :jobIds")
    List<Job> findJobsByIdIn(List<Integer> jobIds);
}
