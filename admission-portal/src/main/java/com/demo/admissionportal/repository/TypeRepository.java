package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Type repository.
 */
@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
    /**
     * Find type by id type.
     *
     * @param id the id
     * @return the type
     */
    Type findTypeById(Integer id);
}
