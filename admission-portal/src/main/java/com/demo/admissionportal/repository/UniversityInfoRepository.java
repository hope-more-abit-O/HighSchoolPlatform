package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface University info repository.
 */
@Repository
public interface UniversityInfoRepository extends JpaRepository<UniversityInfo, Integer> {
    /**
     * Find university info by id university info.
     *
     * @param id the id
     * @return the university info
     */
    UniversityInfo findUniversityInfoById(Integer id);

    /**
     * Find first by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<UniversityInfo> findFirstByCode(String code);

    /**
     * Find university info by consultant id university info.
     *
     * @param id the id
     * @return the university info
     */
    @Query(value = "SELECT ui.* " +
            "FROM [university_info] ui " +
            "LEFT JOIN [consultant_info] ci ON ci.university_id = ui.university_id " +
            "WHERE ci.consultant_id = :id", nativeQuery = true)
    UniversityInfo findUniversityInfoByConsultantId(Integer id);
}
