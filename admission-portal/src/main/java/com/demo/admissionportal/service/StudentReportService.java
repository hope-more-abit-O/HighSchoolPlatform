package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.request.student_report.UpdateStudentReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.CreateStudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.ListStudentReportResponse;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.UpdateStudentReportResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * The interface Student report service.
 */
public interface StudentReportService {
    /**
     * Create student report response data.
     *
     * @param request        the request
     * @param authentication the authentication
     * @return the response data
     */
    ResponseData<CreateStudentReportResponseDTO> createStudentReport(CreateStudentReportRequest request, Authentication authentication);

    /**
     * Update student report by id response data.
     *
     * @param studentReportId the student report id
     * @param request         the request
     * @param authentication  the authentication
     * @return the response data
     */
    ResponseData<UpdateStudentReportResponseDTO> updateStudentReportById(Integer studentReportId, UpdateStudentReportRequest request, Authentication authentication);

    /**
     * Find student report by id response data.
     *
     * @param studentReportId the student report id
     * @param authentication  the authentication
     * @return the response data
     */
    ResponseData<StudentReportResponseDTO> findStudentReportById(Integer studentReportId, Authentication authentication);

    /**
     * Find all student reports response data.
     *
     * @param name           the name
     * @param pageable       the pageable
     * @param authentication the authentication
     * @return the response data
     */
    ResponseData<?> findAllStudentReports(String name, Pageable pageable, Authentication authentication);

    /**
     * Delete student report by id response data.
     *
     * @param studentReportId the student report id
     * @param authentication  the authentication
     * @return the response data
     */
    ResponseData<Void> deleteStudentReportById(Integer studentReportId, Authentication authentication);

}
