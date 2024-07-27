package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.MethodStatus;
import com.demo.admissionportal.entity.Method;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MethodRepository extends JpaRepository<Method, Integer> {
    Optional<Method> findByNameLike(String name);

    Optional<Method> findFirstByName(String methodName);

    Optional<Method> findFirstByNameAndCode(String methodName, String methodCode);

    Optional<Method> findFirstByNameOrCode(String methodName, String methodCode);

    List<Method> findByNameInOrCodeIn(Set<String> methodNames, Set<String> methodCodes);

    List<Method> findByIdIn(List<Integer> ids);

    List<Method> findByCodeIn(Set<String> methodCodes);

    List<Method> findByNameIn(Set<String> methodNames);

    @Query(value = """
        SELECT m.*
        FROM [method] m
        WHERE (:id IS NULL OR m.id = :id)
            AND (:code IS NULL OR LOWER(m.code) LIKE LOWER(CONCAT('%', :code, '%')))
            AND (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:createTime IS NULL OR m.create_time = :createTime)
            AND (:createBy IS NULL OR m.create_by = :createBy)
            AND (:updateTime IS NULL OR m.update_time = :updateTime)
            AND (:updateBy IS NULL OR m.update_by = :updateBy)
            AND (:status IS NULL OR m.status = :status)
    """, nativeQuery = true)
    Page<Method> findMethodBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("code") String code,
            @Param("name") String name,
            @Param("createTime") Date createTime,
            @Param("createBy") Integer createBy,
            @Param("updateTime") Date updateTime,
            @Param("updateBy") Integer updateBy,
            @Param("status") MethodStatus status
    );

    boolean existsByNameAndCode(String name, String code);
}
