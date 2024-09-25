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
     * @param jobName  the job name
     * @param status   the status
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT * " +
            "FROM job j " +
            "WHERE (:jobName IS NULL OR j.name COLLATE Latin1_General_CI_AI LIKE N'%' + :jobName + '%'COLLATE Latin1_General_CI_AI)" +
            "AND (:status IS NULL OR j.status = :status)", nativeQuery = true)
    Page<Job> findJobs(@Param("jobName") String jobName, String status, Pageable pageable);

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
