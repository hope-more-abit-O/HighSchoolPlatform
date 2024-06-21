package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.redis.ResetPasswordAccountRedisCacheDTO;
import com.demo.admissionportal.entity.Staff;
import com.demo.admissionportal.entity.Student;
import com.demo.admissionportal.entity.University;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The interface Otp serivice.
 */
public interface OTPService {
    /**
     * Save otp.
     *
     * @param email        the email
     * @param otp          the otp
     * @param timeGenerate the time generate
     */
    void saveOTP(String email, String otp, LocalDateTime timeGenerate);
    /**
     * Gets otp.
     *
     * @param email the email
     * @return the otp
     */
    String getOTP(String email);

    /**
     * Delete otp.
     *
     * @param email the email
     */
    void deleteOTP(String email);

    /**
     * Gets otp date time.
     *
     * @param email the email
     * @return the otp date time
     */
    LocalDateTime getOTPDateTime(String email);

    /**
     * Save student.
     *
     * @param email   the email
     * @param student the student
     */
    void saveStudent(String email, Student student);

    /**
     * Gets student.
     *
     * @param email the email
     * @return the student
     */
    Student getStudent(String email);

    /**
     * Save university.
     *
     * @param email      the email
     * @param university the university
     */
    void saveUniversity(String email, University university);

    /**
     * Gets university.
     *
     * @param email the email
     * @return the university
     */
    University getUniversity(String email);




}
