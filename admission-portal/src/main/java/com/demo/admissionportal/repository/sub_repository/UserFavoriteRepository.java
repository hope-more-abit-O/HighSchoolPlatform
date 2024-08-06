package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.UserFavorite;
import com.demo.admissionportal.entity.sub_entity.id.UserFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    Integer totalFavoriteCount(Integer universityId);
}
