package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Find by email optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByEmail(String username);

    /**
     * Find by role optional.
     *
     * @param role the role
     * @return the optional
     */
    List<User> findByRole(Role role);


    /**
     * Find user by id user.
     *
     * @param id the id
     * @return the user
     */
    User findUserById(Integer id);

    /**
     * Find first by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findFirstByUsername(String username);

    /**
     * Find first by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<User> findFirstByEmail(String email);

    /**
     * Find user by email user.
     *
     * @param email the email
     * @return the user
     */
    User findUserByEmail(String email);

    /**
     * Find user page.
     *
     * @param pageable the pageable
     * @param username the username
     * @return the page
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    Page<User> findUser(Pageable pageable, @Param("username") String username);

    /**
     * Find by id in list.
     *
     * @param ids the ids
     * @return the list
     */
    List<User> findByIdIn(Collection<Integer> ids);

    /**
     * Find last staff + number username string.
     *
     * @return the string
     */
    @Query("SELECT u.username FROM User u WHERE u.username LIKE 'staff%' ORDER BY u.username DESC LIMIT 1")
    String findLastStaffUsername();

    List<User> findByCreateBy(Integer createBy);

    List<User> findByCreateByAndRole(Integer createBy, Role role);
}
