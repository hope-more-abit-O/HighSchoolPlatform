package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    Post findFirstById(Integer id);


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
    @Query(value = "SELECT p.*, pr2.name " +
            "FROM post p " +
            "LEFT JOIN [user] u ON p.create_by = u.id " +
            "LEFT JOIN consultant_info ci ON u.id = ci.consultant_id " +
            "LEFT JOIN province pr1 ON ci.province_id = pr1.id " +
            "LEFT JOIN staff_info si ON u.id = si.staff_id " +
            "LEFT JOIN province pr2 ON si.province_id = pr2.id " +
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
     * Search post page.
     *
     * @param title      the title
     * @param tag        the tag
     * @param schoolName the school name
     * @param code       the code
     * @param pageable   the pageable
     * @return the page
     */
    @Query(value = "SELECT p.* " +
            "FROM post p " +
            "JOIN post_tag pt ON p.id = pt.post_id " +
            "JOIN tag t ON pt.tag_id = t.id " +
            "LEFT JOIN consultant_info ci ON p.create_by = ci.consultant_id " +
            "LEFT JOIN staff_info si ON p.create_by = si.staff_id " +
            "LEFT JOIN [user] u ON u.id = p.create_by " +
            "LEFT JOIN  university_info ui ON ci.university_id = ui.university_id " +
            "LEFT JOIN  university_campus uc ON ui.university_id = uc.university_id " +
            "JOIN province pr ON uc.province_id = pr.id OR si.province_id = pr.id " +
            "WHERE (:title IS NULL OR ui.code LIKE :title) OR " +
            "(:schoolName IS NULL OR CONCAT(ui.name, ' ', uc.campus_name) LIKE :schoolName) OR " +
            "(:tag IS NULL OR t.name LIKE :tag) OR " +
            "(:code IS NULL OR p.title LIKE :code)", nativeQuery = true)
    Page<Post> searchPost(@Param("title") String title, @Param("tag") String tag, @Param("schoolName") String schoolName, @Param("tag") String code, Pageable pageable);
}
