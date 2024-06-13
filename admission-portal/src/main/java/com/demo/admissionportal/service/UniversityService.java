package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.university.StaffRegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;

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
}
