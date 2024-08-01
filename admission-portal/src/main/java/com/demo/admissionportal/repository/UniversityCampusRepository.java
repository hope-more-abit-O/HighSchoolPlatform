package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UniversityCampus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    UniversityCampus findUniversityCampusByUniversityId(Integer id);

    /**
     * Find list university campus by university id list.
     *
     * @param id the id
     * @return the list
     */
    @Query(value =
            "SELECT * " +
                    "FROM university_campus uc " +
                    "WHERE uc.university_id = :id", nativeQuery = true)
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
}
