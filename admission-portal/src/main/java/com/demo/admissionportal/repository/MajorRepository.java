package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findByName(String methodName);

    List<Major> findByNameInOrCodeIn(Set<String> majorNames, Set<String> majorCodes);

    List<Major> findByIdIn(Collection<Integer> ids);

    List<Major> findByNameIn(Set<String> majorsNames);

    List<Major> findByCodeIn(Set<String> majorCodes);

    @Query(value = """
        SELECT m.*
        FROM [major] m
        WHERE (:id IS NULL OR m.id = :id)
            AND (:code IS NULL OR LOWER(m.code) LIKE LOWER(CONCAT('%', :code, '%')))
            AND (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:note IS NULL OR LOWER(m.note) LIKE LOWER(CONCAT('%', :note, '%')))
            AND (:status IS NULL OR m.status = :status)
            AND (:createBy IS NULL OR m.create_by = :createBy)
            AND (:updateBy IS NULL OR m.update_by = :updateBy)
            AND (:createTime IS NULL OR m.create_time = :createTime)
            AND (:updateTime IS NULL OR m.update_time = :updateTime)
            AND (:createTimeFrom IS NULL OR m.[create_time] >= :createTimeFrom)
            AND (:createTimeTo IS NULL OR m.[create_time] <= :createTimeTo)
            AND (:updateTimeFrom IS NULL OR m.[update_time] >= :updateTimeFrom)
            AND (:updateTimeTo IS NULL OR m.[update_time] <= :updateTimeTo)
    """,nativeQuery = true)
    Page<Major> findMajorBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("code") String code,
            @Param("name") String name,
            @Param("note") String note,
            @Param("status") String status,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy,
            @Param("createTime") Date createTime,
            @Param("updateTime") Date updateTime,
            @Param("createTimeFrom") Date createTimeFrom,
            @Param("createTimeTo") Date createTimeTo,
            @Param("updateTimeFrom") Date updateTimeFrom,
            @Param("updateTimeTo") Date updateTimeTo
    );

    @Query(value = """
        SELECT m.*
        FROM [major] m
        WHERE (:id IS NULL OR m.id = :id)
            AND (:code IS NULL OR LOWER(m.code) LIKE LOWER(CONCAT('%', :code, '%')))
            AND (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:note IS NULL OR LOWER(m.note) LIKE LOWER(CONCAT('%', :note, '%')))
            AND (m.status IN (:status))
            AND (:createBy IS NULL OR m.create_by = :createBy)
            AND (:updateBy IS NULL OR m.update_by = :updateBy)
            AND (:createTime IS NULL OR m.create_time = :createTime)
            AND (:updateTime IS NULL OR m.update_time = :updateTime)
    """, nativeQuery = true)
    Page<Major> findMajorBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("code") String code,
            @Param("name") String name,
            @Param("note") String note,
            @Param("status") List<String> status,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy,
            @Param("createTime") Date createTime,
            @Param("updateTime") Date updateTime
    );

    @Query(value = """
        SELECT m.*
        FROM [major] m
        WHERE (:id IS NULL OR m.id = :id)
            AND (:code IS NULL OR LOWER(m.code) LIKE LOWER(CONCAT('%', :code, '%')))
            AND (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:note IS NULL OR LOWER(m.note) LIKE LOWER(CONCAT('%', :note, '%')))
            AND (:createBy IS NULL OR m.create_by = :createBy)
            AND (:updateBy IS NULL OR m.update_by = :updateBy)
            AND (:createTime IS NULL OR m.create_time = :createTime)
            AND (:updateTime IS NULL OR m.update_time = :updateTime)
    """, nativeQuery = true)
    Page<Major> findMajorBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("code") String code,
            @Param("name") String name,
            @Param("note") String note,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy,
            @Param("createTime") Date createTime,
            @Param("updateTime") Date updateTime
    );

    @Query(value = """
        SELECT m.*
        FROM [major] m
        WHERE (:id IS NULL OR m.id = :id)
            AND (:code IS NULL OR LOWER(m.code) LIKE LOWER(CONCAT('%', :code, '%')))
            AND (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:note IS NULL OR LOWER(m.note) LIKE LOWER(CONCAT('%', :note, '%')))
            AND (:status IS NULL OR m.status = :status)
            AND (:createBy IS NULL OR m.create_by = :createBy)
            AND (:updateBy IS NULL OR m.update_by = :updateBy)
    """, nativeQuery = true)
    Page<Major> findMajorBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("code") String code,
            @Param("name") String name,
            @Param("note") String note,
            @Param("status") String status,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy
    );

    List<Major> findByNameOrCode(String name, String code);
}
