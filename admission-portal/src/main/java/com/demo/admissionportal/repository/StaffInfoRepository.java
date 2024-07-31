package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.dto.entity.staff.FindAllStaffDTO;
import com.demo.admissionportal.entity.StaffInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * The interface Staff info repository.
 */
public interface StaffInfoRepository extends JpaRepository<StaffInfo, Integer> {
    /**
     * Find all page.
     *
     * @param username     the username
     * @param name         the name
     * @param email        the email
     * @param phone        the phone
     * @param statusString the status
     * @param pageable     the pageable
     * @return the page
     */
    @Query("SELECT new com.demo.admissionportal.dto.entity.staff.FindAllStaffDTO(" +
            "u.id, u.username, u.email, CONCAT(s.firstName, ' ', s.middleName, ' ', s.lastName), " +
            "u.avatar, s.phone, u.status, p.name, u.note, u.createTime) " +
            "FROM StaffInfo s " +
            "JOIN s.user u " +
            "LEFT JOIN Province p ON s.provinceId = p.id " +
            "WHERE " +
            "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
            "(:name IS NULL OR LOWER(CONCAT(s.firstName, ' ', s.middleName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phone IS NULL OR LOWER(s.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) AND " +
            "(:statusString IS NULL OR u.status = :statusString)")
    Page<FindAllStaffDTO> findAllWithUserFields(@Param("username") String username,
                                                @Param("name") String name,
                                                @Param("email") String email,
                                                @Param("phone") String phone,
                                                @Param("statusString") AccountStatus statusString,
                                                Pageable pageable);

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