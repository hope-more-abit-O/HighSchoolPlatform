package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.VerifyAccountRequestDTO;
import com.demo.admissionportal.dto.request.VerifyUpdateUniversityRequestDTO;
import com.demo.admissionportal.dto.request.university.StaffRegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.request.university.UpdateUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.University;

/**
 * The interface University service.
 */
public interface UniversityService {

    /**
     * Creates a new university with a predefined "fail" status.
     *
     * @param request The request containing university information and staff id, of type {@link StaffRegisterUniversityRequestDTO}.
     * @return A response indicating the result of the creation process.
     */
    public ResponseData<?> createUniversityFail(StaffRegisterUniversityRequestDTO request);

    /**
     * Creates a new university and associates it with a staff member.
     *
     * @param request The request containing university information and staff id, of type {@link StaffRegisterUniversityRequestDTO}.
     * @return A response containing the created university and staff-university association.
     */
    public ResponseData<?> staffCreateUniversity(StaffRegisterUniversityRequestDTO request);

    /**
     * Gets university by id.
     *
     * @param id the id
     * @return the university by id
     */
//TODO JAVADOC
    public University getUniversityById(Integer id);


    /**
     * Update university response data.
     *
     * @param updateUniversityRequestDTO the update university request dto
     * @return the response data
     */
    ResponseData<University> updateUniversity(UpdateUniversityRequestDTO updateUniversityRequestDTO);

    /**
     * Verify account response data.
     *
     * @param verifyUpdateUniversityRequestDTO the verify update university request dto
     * @return the response data
     */
    ResponseData<?> verifyAccount(VerifyUpdateUniversityRequestDTO verifyUpdateUniversityRequestDTO);
}
