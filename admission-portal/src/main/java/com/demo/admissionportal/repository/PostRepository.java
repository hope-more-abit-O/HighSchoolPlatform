package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * The interface Post repository.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    /**
     * Find post by id post.
     *
     * @param id the id
     * @return the post
     */
    @Query(value = "SELECT * " +
            "FROM [post] p " +
            "WHERE p.status = 'ACTIVE' AND p.id = :id", nativeQuery = true)
    Post findPostWithActive(Integer id);

    /**
     * Find post with status post.
     *
     * @param id the id
     * @return the post
     */
    @Query(value = "SELECT * " +
            "FROM [post] p " +
            "WHERE p.id = :id", nativeQuery = true)
    Post findPostWithNoStatus(Integer id);


    /**
     * Find post list.
     *
     * @return the list
     */
    @Query(value = "SELECT p.* FROM post p " +
            "INNER JOIN post_type pt ON p.id = pt.post_id " +
            "INNER JOIN type t ON pt.type_id = t.id " +
            "INNER JOIN [user] u ON p.create_by = u.id " +
            "AND p.status = 'ACTIVE'", nativeQuery = true)
    List<Post> findPost();


    /**
     * Find post by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query(value = "SELECT p.* FROM post p " +
            "INNER JOIN post_type pt ON p.id = pt.post_id " +
            "INNER JOIN type t ON pt.type_id = t.id " +
            "INNER JOIN [user] u ON p.create_by = u.id " +
            "WHERE p.create_by = :userId", nativeQuery = true)
    List<Post> findPostByUserId(@Param("userId") Integer userId);

    /**
     * Find post with staff info list.
     *
     * @param title    the title
     * @param status   the status
     * @param pageable the pageable
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM post p " +
            "JOIN staff_info si ON p.create_by = si.staff_id " +
            "WHERE (:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:status IS NULL OR p.status = :status)", nativeQuery = true)
    Page<Post> findPostWithStaffInfo(@Param("title") String title, @Param("title") String status, Pageable pageable);


    /**
     * Find all by staff id post.
     *
     * @param createBy the create by
     * @param postId   the post id
     * @return the post
     */
    @Query(value = "SELECT p.* " +
            "FROM post p " +
            "JOIN staff_info si ON p.create_by = si.staff_id " +
            "WHERE p.create_by = :createBy AND p.id = :postId",
            nativeQuery = true)
    Post findAllByStaffId(@Param("createBy") Integer createBy, @Param("postId") Integer postId);

    /**
     * Find all by uni id post.
     *
     * @param title    the title
     * @param status   the status
     * @param createBy the create by
     * @param pageable the pageable
     * @return the post
     */
    @Query(value = "SELECT * " +
            "FROM post p " +
            "JOIN consultant_info ci ON p.create_by = ci.consultant_id " +
            "WHERE ci.university_id = :createBy " +
            "AND (:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:status IS NULL OR p.status = :status)", nativeQuery = true)
    Page<Post> findAllByUniId(@Param("title") String title, @Param("status") String status, @Param("createBy") Integer createBy, Pageable pageable);

    /**
     * Find post with location id list.
     *
     * @param locationId the location id
     * @return the list
     */
    @Query(value = "SELECT p.*, pr2.name, pr1.name " +
            "FROM post p " +
            "LEFT JOIN [user] u ON p.create_by = u.id " +
            "LEFT JOIN [staff_info] si ON u.id = si.staff_id " +
            "LEFT JOIN [consultant_info] ci ON p.create_by = ci.consultant_id " +
            "LEFT JOIN [university_info] ui ON ci.university_id = ui.university_id " +
            "LEFT JOIN [university_campus] uc ON ui.university_id = uc.university_id " +
            "LEFT JOIN [province] pr1 ON uc.province_id = pr1.id " +
            "LEFT JOIN [province] pr2 ON si.province_id = pr2.id " +
            "WHERE (pr1.id = :locationId OR pr2.id = :locationId) " +
            "AND p.status = 'ACTIVE'", nativeQuery = true)
    List<Post> findPostWithLocationId(@Param("locationId") Integer locationId);

    /**
     * Find all with no location id list.
     *
     * @return the list
     */
    @Query(value = "SELECT p.* FROM post p " +
            "WHERE p.status = 'ACTIVE'", nativeQuery = true)
    List<Post> findAllWithStatus();

    /**
     * Find first by url post.
     *
     * @param url the url
     * @return the post
     */
    @Query(value = "SELECT * " +
            "FROM [post] p " +
            "WHERE p.url = :url AND p.status = 'ACTIVE'", nativeQuery = true)
    Post findFirstByUrl(String url);

    /**
     * Find all by create by in list.
     *
     * @param universityId the university id
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM [post] p " +
            "LEFT JOIN [consultant_info] ci ON p.create_by = ci.consultant_id " +
            "WHERE ci.university_id = :universityId AND p.status = 'ACTIVE'" +
            "ORDER BY p.create_time DESC", nativeQuery = true)
    List<Post> findAllByCreateByIn(Integer universityId);

    /**
     * Find post has package list.
     *
     * @return the list
     */
    @Query(value = "SELECT p.* " +
            "FROM post p " +
            "LEFT JOIN ( " +
            "    SELECT post_id, MAX(complete_time) AS max_complete_time " +
            "    FROM university_package " +
            "    WHERE status = 'ACTIVE' " +
            "    GROUP BY post_id " +
            ") max_up " +
            "ON p.id = max_up.post_id " +
            "LEFT JOIN university_package up " +
            "ON up.post_id = p.id " +
            "AND up.complete_time = max_up.max_complete_time " +
            "WHERE up.status = 'ACTIVE' AND p.status = 'ACTIVE' " +
            "AND up.complete_time >= GETDATE()" +
            "ORDER BY up.create_time DESC ", nativeQuery = true)
    List<Post> findPostHasPackage();

    /**
     * Total post integer.
     *
     * @return the integer
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM post", nativeQuery = true)
    Optional<Integer> totalPost();

    /**
     * Current post integer.
     *
     * @return the integer
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM post p " +
            "WHERE p.status = 'ACTIVE' AND CONVERT(DATE, p.create_time) = CONVERT(DATE, GETDATE())", nativeQuery = true)
    Optional<Integer> currentPost();

    /**
     * Total post with uni id optional.
     *
     * @param universityId the university id
     * @return the optional
     */
    @Query(value = "SELECT COUNT(p.id) " +
            "FROM university_info ui " +
            "JOIN consultant_info ci ON ci.university_id = ui.university_id " +
            "JOIN post p ON p.create_by = ci.consultant_id OR p.create_by = ui.university_id " +
            "WHERE ui.university_id = :universityId", nativeQuery = true)
    Optional<Integer> totalPostWithUniId(Integer universityId);

    /**
     * Active post optional.
     *
     * @return the optional
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM post p " +
            "WHERE p.status = 'ACTIVE'", nativeQuery = true)
    Optional<Integer> activePost();

    /**
     * Inactive post optional.
     *
     * @return the optional
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM post p " +
            "WHERE p.status = 'INACTIVE'", nativeQuery = true)
    Optional<Integer> inactivePost();
}
