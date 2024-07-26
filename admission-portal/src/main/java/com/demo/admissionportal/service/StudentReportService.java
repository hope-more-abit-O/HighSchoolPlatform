package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.request.student_report.UpdateStudentReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.CreateStudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.UpdateStudentReportResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;


/**
 * <h1>Student Report Service Interface</h1>
 * <p>
 * This interface defines the operations related to managing student reports within the Admission Portal.
 * It provides methods for creating, updating, retrieving, and deleting student reports.
 * Implementations of this interface are responsible for the business logic associated with student report
 * management and interactions with the underlying data layer.
 * </p>
 * <p>
 * The following operations are supported:
 * <ul>
 *     <li>Creating a new student report</li>
 *     <li>Updating an existing student report</li>
 *     <li>Retrieving details of a specific student report by its ID</li>
 *     <li>Listing all student reports with optional filters</li>
 *     <li>Deleting a student report</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
public interface StudentReportService {
    /**
     * <h2>Create Student Report</h2>
     * <p>
     * Creates a new student report based on the provided request data. The request must contain valid student
     * information and the details of the report. The authenticated user information is used to track who created
     * the report.
     * </p>
     *
     * @param request        The {@link CreateStudentReportRequest} containing the details of the new student report.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the creation operation along with the created {@link CreateStudentReportResponseDTO}.
     * @since 1.0
     */
    ResponseData<CreateStudentReportResponseDTO> createStudentReport(CreateStudentReportRequest request, Authentication authentication);

    /**
     * <h2>Update Student Report By ID</h2>
     * <p>
     * Updates the details of an existing student report identified by the provided ID. The request contains the updated
     * report details. The authenticated user information is used to track who performed the update.
     * </p>
     *
     * @param studentReportId The ID of the student report to be updated.
     * @param request         The {@link UpdateStudentReportRequest} containing the updated details of the student report.
     * @param authentication  The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the update operation along with the updated {@link UpdateStudentReportResponseDTO}.
     * @since 1.0
     */
    ResponseData<UpdateStudentReportResponseDTO> updateStudentReportById(Integer studentReportId, UpdateStudentReportRequest request, Authentication authentication);

    /**
     * <h2>Find Student Report By ID</h2>
     * <p>
     * Retrieves the details of a specific student report identified by the provided ID. The response includes all the
     * report details along with the information about who created the report. The authenticated user information is
     * used to verify permissions.
     * </p>
     *
     * @param studentReportId The ID of the student report to be retrieved.
     * @param authentication  The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the details of the student report along with {@link StudentReportResponseDTO}.
     * @since 1.0
     */
    ResponseData<StudentReportResponseDTO> findStudentReportById(Integer studentReportId, Authentication authentication);

    /**
     * <h2>Find All Student Reports</h2>
     * <p>
     * Retrieves a paginated list of all student reports, optionally filtered by student name. The response includes
     * summary details of each student report. The authenticated user information is used to verify permissions.
     * </p>
     *
     * @param name           Optional filter for the student's name.
     * @param pageable       Pagination details.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing a paginated list of student reports.
     * @since 1.0
     */
    ResponseData<?> findAllStudentReports(String name, Pageable pageable, Authentication authentication);

    /**
     * <h2>Delete Student Report By ID</h2>
     * <p>
     * Deletes a specific student report identified by the provided ID. The report will no longer be available in the system.
     * The authenticated user information is used to verify permissions.
     * </p>
     *
     * @param studentReportId The ID of the student report to be deleted.
     * @param authentication  The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the deletion operation.
     * @since 1.0
     */
    ResponseData<Void> deleteStudentReportById(Integer studentReportId, Authentication authentication);
}
