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

    /**
     * Find by code in list.
     *
     * @param codes the codes
     * @return the list
     */
    List<UniversityInfo> findByCodeIn(Collection<String> codes);

    /**
     * Find by staff id list.
     *
     * @param staffId the staff id
     * @return the list
     */
    List<UniversityInfo> findByStaffId(Integer staffId);

    /**
     * Find by name list.
     *
     * @return the list
     */
    @Query(value = "SELECT ui.*, u.avatar " +
            "FROM university_info ui " +
            "JOIN [user] u ON ui.university_id = u.id " +
            "WHERE u.status = 'ACTIVE' " +
            "AND ui.name LIKE CONCAT('%', :uniName, '%')", nativeQuery = true)
    List<UniversityInfo> findByName(@Param(value = "uniName") String uniName);
}
