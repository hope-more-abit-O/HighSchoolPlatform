package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Ward repository.
 */
@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    /**
     * Find ward by id ward.
     *
     * @param id the id
     * @return the ward
     */
    Ward findWardById(Integer id);
}
