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
            "INNER JOIN [user] u ON p.create_by = u.id ", nativeQuery = true)
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
     * @param pageable the pageable
     * @return the list
     */
    @Query(value = "SELECT * " +
            "FROM post p " +
            "JOIN staff_info si ON p.create_by = si.staff_id " +
            "WHERE :title IS NULL OR p.title LIKE %:title%", nativeQuery = true)
    Page<Post> findPostWithStaffInfo(@Param("title") String title, Pageable pageable);


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
     * @param createBy the create by
     * @param pageable the pageable
     * @return the post
     */
    @Query(value = "SELECT * " +
            "FROM post p " +
            "JOIN consultant_info ci ON p.create_by = ci.consultant_id " +
            "WHERE ci.university_id = :createBy " +
            "AND :title IS NULL OR p.title LIKE %:title%", nativeQuery = true)
    Page<Post> findAllByUniId(@Param("title") String title, @Param("createBy") Integer createBy, Pageable pageable);
}
