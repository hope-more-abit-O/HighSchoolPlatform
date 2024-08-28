package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.CampusType;
import com.demo.admissionportal.constants.UniversityCampusStatus;
import com.demo.admissionportal.entity.UniversityCampus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The interface University campus repository.
 */
@Repository
public interface UniversityCampusRepository extends JpaRepository<UniversityCampus, Integer> {
    /**
     * Find university campus by id university campus.
     *
     * @param id the id
     * @return the university campus
     */
    @Query(value = "SELECT * " +
            "FROM university_campus uc " +
            "WHERE uc.university_id = :id AND type = 'HEADQUARTERS'", nativeQuery = true)
    UniversityCampus findFirstUniversityCampusByUniversityId(Integer id);

    /**
     * Find list university campus by university id list.
     *
     * @param id the id
     * @return the list
     */
    @Query(value =
                    "SELECT * " +
                    "FROM university_campus uc " +
                    "WHERE uc.university_id = :id AND uc.status = 'ACTIVE' " +
                    "ORDER BY type ASC, id ASC", nativeQuery = true)
    List<UniversityCampus> findListUniversityCampusByUniversityId(Integer id);

    /**
     * Find head quarters campus by university id university campus.
     *
     * @param id the id
     * @return the university campus
     */
    @Query(value =
            "SELECT * " +
                    "FROM university_campus uc " +
                    "WHERE uc.university_id = :id AND uc.type = 'HEADQUARTERS'", nativeQuery = true)
    UniversityCampus findHeadQuartersCampusByUniversityId(Integer id);

    /**
     * Find first by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<UniversityCampus> findFirstByPhone(String phone);

    /**
     * Find university campus by id university campus.
     *
     * @param id the id
     * @return the university campus
     */
    UniversityCampus findUniversityCampusById(Integer id);

    /**
     * Find by university id list.
     *
     * @param universityId the university id
     * @return the list
     */
    List<UniversityCampus> findByUniversityId(Integer universityId);

    List<UniversityCampus> findByUniversityIdInAndStatusAndType(Collection<Integer> universityIds, UniversityCampusStatus status, CampusType type);

    List<UniversityCampus> findByUniversityIdInAndStatus(List<Integer> universityIds, UniversityCampusStatus universityCampusStatus);
}
