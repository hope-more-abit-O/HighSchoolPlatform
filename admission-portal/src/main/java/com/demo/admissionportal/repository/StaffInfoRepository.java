package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.StaffInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * The interface Staff info repository.
 */
public interface StaffInfoRepository extends JpaRepository<StaffInfo, Integer> {
    /**
     * Find all page.
     *
     * @param username     the username
     * @param firstName    the first name
     * @param middleName   the middle name
     * @param lastName     the last name
     * @param email        the email
     * @param phone        the phone
     * @param statusString the status
     * @param pageable     the pageable
     * @return the page
     */
    @Query(value = "SELECT * FROM staff_info s JOIN [user] u ON s.staff_id = u.id WHERE " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:firstName IS NULL OR s.first_name LIKE %:firstName%) AND " +
            "(:middleName IS NULL OR s.middle_name LIKE %:middleName%) AND " +
            "(:lastName IS NULL OR s.last_name LIKE %:lastName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:phone IS NULL OR s.phone LIKE %:phone%) AND " +
            "(:statusString IS NULL OR u.status = :statusString)" +
            "ORDER BY u.create_time DESC", nativeQuery = true)
    Page<StaffInfo> findAll(String username, String firstName,String middleName, String lastName, String email, String phone, String statusString, Pageable pageable);

    /**
     * Find staff info by id staff info.
     *
     * @param id the id
     * @return the staff info
     */
    StaffInfo findStaffInfoById(Integer id);

    /**
     * Find first by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<StaffInfo> findFirstByPhone(String phone);

}