package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.UserFollowMajor;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowMajorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Follow repository.
 */
@Repository
public interface FollowRepository extends JpaRepository<UserFollowMajor, UserFollowMajorId> {
    /**
     * Find by user id and major id user follow major.
     *
     * @param userId  the user id
     * @param majorId the major id
     * @return the user follow major
     */
    UserFollowMajor findByUserIdAndMajorId(Integer userId, Integer majorId);

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<UserFollowMajor> findByUserId(Integer userId);
}
