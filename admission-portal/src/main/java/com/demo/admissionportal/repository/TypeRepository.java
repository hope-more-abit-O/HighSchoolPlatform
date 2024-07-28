package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Type;
import io.lettuce.core.dynamic.annotation.Param;
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

    /**
     * Find all type page.
     *
     * @param name     the name
     * @param status   the status
     * @param pageable the pageable
     * @return the page
     */
    @Query(value = "SELECT t.* " +
            "FROM [type] t " +
            "WHERE (:name IS NULL OR t.name LIKE %:name%) " +
            "AND (:status IS NULL OR t.status = :status)", nativeQuery = true)
    Page<Type> findAllType(@Param("name") String name, @Param("status") String status, Pageable pageable);
}
