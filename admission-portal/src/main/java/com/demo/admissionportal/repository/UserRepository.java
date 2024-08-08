package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    Page<User> findByRole(Role role, Pageable pageable);
    Page<User> findByCreateByAndRole(Integer createBy, Role role, Pageable pageable);

    @Query(value = """
        SELECT usr.*
        FROM [user] usr
        INNER JOIN university_info u ON usr.id = u.university_id
        LEFT JOIN university_campus unic ON unic.university_id = u.university_id
        WHERE (:id IS NULL OR usr.id = :id)
          AND (:username IS NULL OR LOWER(usr.username) LIKE LOWER(CONCAT('%', :username, '%')))
          AND (:code IS NULL OR LOWER(u.code) LIKE LOWER(CONCAT('%', :code, '%')))
          AND (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(unic.campus_name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:phone IS NULL OR unic.phone LIKE %:phone%)
          AND (:email IS NULL OR LOWER(usr.email) LIKE LOWER(CONCAT('%', :email, '%')) OR LOWER(unic.email) LIKE LOWER(CONCAT('%', :email, '%')))
          AND (:status IS NULL OR LOWER(usr.status) = LOWER(:status))
          AND (:createBy IS NULL OR usr.create_by = :createBy)
    """, nativeQuery = true)
    Page<User> findUniversityAccountBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("code") String code,
            @Param("username") String username,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("status") String status,
            @Param("createBy") Integer createBy
    );
    @Query(value = """
        SELECT distinct usr.*
        FROM [user] usr
        INNER JOIN university_info u ON usr.id = u.university_id
        INNER JOIN staff_info si ON si.staff_id = usr.create_by 
        LEFT JOIN university_campus unic ON unic.university_id = u.university_id
        WHERE (:id IS NULL OR usr.id = :id)
            AND (:username IS NULL OR LOWER(usr.username) LIKE LOWER(CONCAT('%', :username, '%')))
            AND (:code IS NULL OR LOWER(u.code) LIKE LOWER(CONCAT('%', :code, '%')))
            AND (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(unic.campus_name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:phone IS NULL OR unic.phone LIKE %:phone%)
            AND (:email IS NULL OR LOWER(usr.email) LIKE LOWER(CONCAT('%', :email, '%')) OR LOWER(unic.email) LIKE LOWER(CONCAT('%', :email, '%')))
            AND (:status IS NULL OR LOWER(usr.status) = LOWER(:status))
            AND (:createBy IS NULL OR usr.create_by = :createBy)
            AND (:createByName IS NULL OR LOWER(CONCAT(COALESCE(si.first_name, ''), ' ', COALESCE(si.middle_name, ''), ' ', COALESCE(si.last_name, ''))) LIKE LOWER(CONCAT('%', :createByName, '%')))
 """, nativeQuery = true)
    Page<User> findUniversityAccountBy(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("code") String code,
            @Param("username") String username,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("status") String status,
            @Param("createBy") Integer createBy,
            @Param("createByName") String createByName
    );

    @Query(value = """
    SELECT usr.*
    FROM [user] usr
    INNER JOIN [consultant_info] ci ON ci.consultant_id = usr.id
    LEFT JOIN [university_info] uni ON uni.university_id = ci.university_id
    WHERE (:id IS NULL OR usr.id = :id)
        AND (:name IS NULL OR LOWER(CONCAT(COALESCE(ci.first_name, ''), ' ', COALESCE(ci.middle_name, ''), ' ', COALESCE(ci.last_name, ''))) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:username IS NULL OR LOWER(usr.username) LIKE LOWER(CONCAT('%', :username, '%')))
        AND (:universityName IS NULL OR LOWER(uni.name) LIKE LOWER(CONCAT('%', :universityName, '%')))
        AND (:universityId IS NULL OR uni.university_id = :universityId)
        AND (usr.status IN (:status))
        AND (:createBy IS NULL OR usr.create_by = :createBy)
        AND (:updateBy IS NULL OR usr.update_by = :updateBy)
        AND (usr.role = 'CONSULTANT')
    """, nativeQuery = true)
    Page<User> getConsultantAccount(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("username") String username,
            @Param("universityName") String universityName,
            @Param("universityId") Integer universityId,
            @Param("status") List<String> status,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy
    );

    @Query(value = """
    SELECT usr.*
    FROM [user] usr
    INNER JOIN [consultant_info] ci ON ci.consultant_id = usr.id
    LEFT JOIN [university_info] uni ON uni.university_id = ci.university_id
    WHERE (:id IS NULL OR usr.id = :id)
        AND (:name IS NULL OR LOWER(CONCAT(COALESCE(ci.first_name, ''), ' ', COALESCE(ci.middle_name, ''), ' ', COALESCE(ci.last_name, ''))) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:username IS NULL OR LOWER(usr.username) LIKE LOWER(CONCAT('%', :username, '%')))
        AND (:universityName IS NULL OR LOWER(uni.name) LIKE LOWER(CONCAT('%', :universityName, '%')))
        AND (:universityId IS NULL OR uni.university_id = :universityId)
        AND (:createBy IS NULL OR usr.create_by = :createBy)
        AND (:updateBy IS NULL OR usr.update_by = :updateBy)
        AND (usr.role = 'CONSULTANT')
    """, nativeQuery = true)
    Page<User> getConsultantAccount(
            Pageable pageable,
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("username") String username,
            @Param("universityName") String universityName,
            @Param("universityId") Integer universityId,
            @Param("createBy") Integer createBy,
            @Param("updateBy") Integer updateBy
    );


    @Query(value = """
    SELECT usr.*
    FROM [user] usr
    INNER JOIN [university_campus] unic ON unic.university_id = usr.id
    WHERE usr.email = :email OR unic.email = :email""", nativeQuery = true)
    Optional<User> validateEmail(@Param("email") String email);

    Optional<User> findByIdAndRole(Integer id, Role role);
}
