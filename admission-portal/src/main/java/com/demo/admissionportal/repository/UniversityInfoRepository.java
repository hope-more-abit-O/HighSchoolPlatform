package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
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

    List<UniversityInfo> findByCodeIn(Collection<String> codes);

    List<UniversityInfo> findByStaffId(Integer staffId);

    @Query("SELECT u FROM UniversityInfo u WHERE u.id = :universityId")
    List<UniversityInfo> findByUniversityId(@Param("universityId")Integer universityId);

    /**
     * Find by name list.
     *
     * @param uniName the uni name
     * @return the list
     */
    @Query(value = "WITH MatchedUniversities AS ( " +
            "    SELECT ui.*, u.avatar " +
            "    FROM university_info ui " +
            "    JOIN [user] u ON ui.university_id = u.id " +
            "    WHERE u.status = 'ACTIVE' " +
            "    AND ui.name COLLATE Latin1_General_CI_AI LIKE N'%' + :uniName + '%' COLLATE Latin1_General_CI_AI " +
            "), " +
            "RandomUniversities AS ( " +
            "    SELECT TOP ( " +
            "        CASE  " +
            "            WHEN (SELECT COUNT(*) FROM MatchedUniversities) < 5  " +
            "            THEN 5 - (SELECT COUNT(*) FROM MatchedUniversities)  " +
            "            ELSE 0  " +
            "        END " +
            "    ) ui.*, u.avatar " +
            "    FROM university_info ui " +
            "    JOIN [user] u ON ui.university_id = u.id " +
            "    WHERE u.status = 'ACTIVE' " +
            "    AND ui.name COLLATE Latin1_General_CI_AI NOT LIKE N'%' + :uniName + '%' COLLATE Latin1_General_CI_AI " +
            "    ORDER BY NEWID() " +
            ") " +
            "SELECT * FROM MatchedUniversities " +
            "UNION ALL " +
            "SELECT * FROM RandomUniversities; ", nativeQuery = true)
    List<UniversityInfo> findByName(@Param("uniName") String uniName);
}
