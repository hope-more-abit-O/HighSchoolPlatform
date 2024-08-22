package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.UserFavorite;
import com.demo.admissionportal.entity.sub_entity.id.UserFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface User favorite repository.
 */
@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UserFavoriteId> {
    /**
     * Find by user id and university id user favorite.
     *
     * @param userId       the user id
     * @param universityId the university id
     * @return the user favorite
     */
    UserFavorite findByUserIdAndUniversityId(Integer userId, Integer universityId);

    /**
     * Total favorite count integer.
     *
     * @param universityId the university id
     * @return the integer
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM [user_favorite] uf " +
            "WHERE uf.status = 'FOLLOW' AND uf.university_id = :universityId ", nativeQuery = true)
    Optional<Integer> totalFavoriteCount(Integer universityId);

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM user_favorite uf " +
            "WHERE uf.user_id = :userId AND uf.status = 'FOLLOW'",nativeQuery = true)
    List<UserFavorite> findByUserId(Integer userId);

    /**
     * Total interaction integer.
     *
     * @return the integer
     */
    @Query(value = "SELECT SUM(count) AS total_count " +
            "FROM (" +
            "    SELECT COUNT(*) AS count " +
            "    FROM user_favorite uf " +
            "    UNION ALL " +
            "    SELECT COUNT(*) AS count " +
            "    FROM user_like ul " +
            ") AS combined_counts;",nativeQuery = true)
    Optional<Integer> totalInteraction();

    /**
     * Current interaction integer.
     *
     * @return the integer
     */
    @Query(value = "    SELECT SUM(count) AS total_count " +
            "    FROM (" +
            "        SELECT COUNT(*) AS count " +
            "        FROM user_favorite uf " +
            "        WHERE CONVERT(DATE, uf.create_time) = CONVERT(DATE, GETDATE()) " +
            "        UNION ALL " +
            "        SELECT COUNT(*) AS count " +
            "        FROM user_like ul " +
            "        WHERE CONVERT(DATE, ul.create_time) = CONVERT(DATE, GETDATE())" +
            "    ) AS combined_counts;",nativeQuery = true)
    Optional<Integer> currentInteraction();
}
