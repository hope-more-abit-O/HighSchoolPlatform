package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Find type by name list.
     *
     * @param name the name
     * @return the list
     */
    List<Type> findTypeByName(String name);

    @Query(value = "SELECT * " +
                   "FROM type", nativeQuery = true)
    Page<Type> findAllType(Pageable pageable);
}
