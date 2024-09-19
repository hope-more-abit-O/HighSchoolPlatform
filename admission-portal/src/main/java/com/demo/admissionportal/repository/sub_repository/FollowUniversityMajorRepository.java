package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.UserFollowMajor;
import com.demo.admissionportal.entity.UserFollowUniversityMajor;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowUniversityMajorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Follow university major repository.
 */
@Repository
public interface FollowUniversityMajorRepository extends JpaRepository<UserFollowUniversityMajor, UserFollowUniversityMajorId> {
    /**
     * Find by user id and university id user follow university major.
     *
     * @param userId            the user id
     * @param universityMajorId the university major id
     * @return the user follow university major
     */
    @Query(value = "SELECT ufum.* " +
            "FROM user_follow_university_major ufum " +
            "JOIN dbo.university_training_program utp on utp.id = ufum.university_major " +
            "WHERE ufum.user_id = :userId AND utp.id = :universityMajorId ", nativeQuery = true)
    UserFollowUniversityMajor findByUserIdAndUniversityMajor(Integer userId, Integer universityMajorId);

    /**
     * Find by user id list.
     *
     * @param userId the user idW
     * @return the list
     */
    List<UserFollowUniversityMajor> findByUserId(Integer userId);

    /**
     * List users follow list.
     *
     * @param universityId the university id
     * @return the list
     */
    @Query(value = "SELECT ufum.* , utp.major_id " +
            "FROM user_follow_university_major ufum " +
            "JOIN dbo.university_training_program utp on ufum.university_major = utp.id " +
            "WHERE utp.university_id = :universityId AND ufum.status = 'FOLLOW'", nativeQuery = true)
    List<UserFollowUniversityMajor> listUsersFollow(Integer universityId);

    /**
     * Find by user id v 2 list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM user_follow_university_major ufum " +
            "WHERE ufum.user_id = :userId AND status = 'FOLLOW'", nativeQuery = true)
    List<UserFollowUniversityMajor> findByUserIdV2(Integer userId);

    /**
     * Find high index list.
     *
     * @param indexOfFollow the index of follow
     * @param userId        the user id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM user_follow_university_major ufum " +
            "WHERE ufum.index_of_follow > :indexOfFollow AND ufum.user_id = :userId ", nativeQuery = true)
    List<UserFollowUniversityMajor> findHighIndex(Integer indexOfFollow, Integer userId);
}
