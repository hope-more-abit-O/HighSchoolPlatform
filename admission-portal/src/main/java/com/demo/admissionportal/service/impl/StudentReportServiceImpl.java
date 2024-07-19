package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.StudentReportStatus;
import com.demo.admissionportal.dto.request.student_report.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.CreateStudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.ListStudentReportResponse;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.UpdateStudentReportResponseDTO;
import com.demo.admissionportal.entity.StudentReport;
import com.demo.admissionportal.entity.SubjectGradeSemester;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.StudentReportMark;
import com.demo.admissionportal.entity.sub_entity.id.StudentReportMarkId;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.StudentReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentReportServiceImpl implements StudentReportService {
    private final UserRepository userRepository;
    private final StudentReportRepository studentReportRepository;
    private final SubjectGradeSemesterRepository subjectGradeSemesterRepository;
    private final StudentReportMarkRepository studentReportMarkRepository;

    @Override
    @Transactional
    public ResponseData<CreateStudentReportResponseDTO> createStudentReport(CreateStudentReportRequest request, Authentication authentication) {
        try {
            //check exist user
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }

            //save student_report data first
            StudentReport studentReport = new StudentReport();
            User student = user.get();
            Integer studentId = student.getId();
            studentReport.setUserId(studentId);
            studentReport.setName(request.getStudentReportName());
            studentReport.setCreateBy(studentId);
            studentReport.setCreateTime(new Date());
            studentReport.setStatus(StudentReportStatus.ACTIVE);
            studentReportRepository.save(studentReport);
            log.info("Save StudentReport: UserId: {}, Name: {}, CreateBy: {}, CreateTime: {}, Status: {}", studentReport.getUserId(), studentReport.getName(), studentReport.getCreateBy(), studentReport.getCreateTime(), studentReport.getStatus());

            //display all grades, semesters and subjects
            List<SubjectGradeSemester> subjectGradeSemesterList = subjectGradeSemesterRepository.findAll();
            List<SubjectReportDTO> subjectReportDTOList = new ArrayList<>();
            //display all grades, semesters and subjects
            for (SubjectGradeSemester sgs : subjectGradeSemesterList) {
                StudentReportMark mark = new StudentReportMark();
                mark.setStudentReportId(studentReport.getId());
                mark.setSubjectGradeSemesterId(sgs.getId());
                mark.setMark(null); // default null to be updated later
                studentReportMarkRepository.save(mark);
                log.info("Saved null mark for SubjectGradeSemester ID: {}, StudentReport ID: {}", sgs.getId(), studentReport.getId());
                //map mark and grade to response
                SubjectReportDTO subjectReportDTO = subjectReportDTOList.stream()
                        .filter(sr -> sr.getSubjectId().equals(sgs.getSubjectId()))
                        .findFirst()
                        .orElseGet(() -> {
                            SubjectReportDTO newSubjectReportDTO = new SubjectReportDTO(sgs.getSubjectId(), new ArrayList<>());
                            subjectReportDTOList.add(newSubjectReportDTO);
                            return newSubjectReportDTO;
                        });

                GradeReportDTO gradeReportDTO = subjectReportDTO.getGrades().stream()
                        .filter(gr -> gr.getGrade().equals(sgs.getGrade()))
                        .findFirst()
                        .orElseGet(() -> {
                            GradeReportDTO newGradeReportDTO = new GradeReportDTO(sgs.getGrade(), new ArrayList<>());
                            subjectReportDTO.getGrades().add(newGradeReportDTO);
                            return newGradeReportDTO;
                        });

                gradeReportDTO.getSemesterMarks().add(new SemesterMarkDTO(sgs.getSemester(), null));
            }
            //create final response and integrate mark and grade for final response
            CreateStudentReportResponseDTO responseDTO = new CreateStudentReportResponseDTO();
            responseDTO.setId(studentReport.getId());
            responseDTO.setStudentId(studentReport.getUserId());
            responseDTO.setName(studentReport.getName());
            responseDTO.setCreateBy(studentReport.getCreateBy());
            responseDTO.setCreateTime(studentReport.getCreateTime());
            responseDTO.setStatus(studentReport.getStatus());
            responseDTO.setReport(subjectReportDTOList);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo học bạ thành công!", responseDTO);
        } catch (Exception e) {
            log.error("Error creating student report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tạo học bạ! Vui lòng thử lại sau.");
        }
    }

    @Override
    @Transactional
    public ResponseData<UpdateStudentReportResponseDTO> updateStudentReportById(Integer studentReportId, UpdateStudentReportRequest request, Authentication authentication) {
        try {
            //check exist user
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            //check exist student report
            Optional<StudentReport> optionalStudentReport = studentReportRepository.findById(studentReportId);
            if (optionalStudentReport.isEmpty()) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Học bạ không được tìm thấy !");
            }
            //check authorize user
            StudentReport studentReport = optionalStudentReport.get();
            User authenticatedUser = user.get();

            if (!studentReport.getUserId().equals(authenticatedUser.getId())) {
                log.info("User {} is not authorized to update StudentReport {}", authenticatedUser.getId(), studentReportId);
                return new ResponseData<>(ResponseCode.C201.getCode(), "Người dùng không được phép cập nhật học bạ này !");
            }

            studentReport.setName(request.getStudentReportName());
            studentReport.setUpdateBy(authenticatedUser.getId());
            studentReport.setUpdateTime(new Date());
            studentReportRepository.save(studentReport);
            //check valid grade, semester and subject exist in table subject_grade_semester
            for (UpdateMarkDTO markDTO : request.getMarks()) {
                Optional<SubjectGradeSemester> subjectGradeSemester = subjectGradeSemesterRepository.findBySubjectIdAndGradeAndSemester(
                        markDTO.getSubjectId(), markDTO.getGrade(), markDTO.getSemester());

                if (subjectGradeSemester.isPresent()) {
                    StudentReportMarkId markId = new StudentReportMarkId(studentReport.getId(), subjectGradeSemester.get().getId());
                    StudentReportMark studentReportMark = studentReportMarkRepository.findById(markId).orElse(new StudentReportMark());
                    studentReportMark.setStudentReportId(studentReport.getId());
                    studentReportMark.setSubjectGradeSemesterId(subjectGradeSemester.get().getId());
                    studentReportMark.setMark(markDTO.getMark());
                    log.info("Subject {}, Grade {}, Semester {} valid", markDTO.getSubjectId(), markDTO.getGrade(), markDTO.getSemester());
                    studentReportMarkRepository.save(studentReportMark);
                } else {
                    log.error("Subject {}, Grade {}, Semester {} not found", markDTO.getSubjectId(), markDTO.getGrade(), markDTO.getSemester());
                    return new ResponseData<>(ResponseCode.C205.getCode(), "Môn học hoặc học kì hoặc khối lớp học không được tìm thấy !");
                }
            }
            //find student report need to update
            List<StudentReportMark> reportMarks = studentReportMarkRepository.findByStudentReportId(studentReportId);
            List<SubjectReportDTO> subjectReportDTOList = new ArrayList<>();
            //map mark and grade to response
            for (StudentReportMark mark : reportMarks) {
                SubjectGradeSemester sgs = subjectGradeSemesterRepository.findById(mark.getSubjectGradeSemesterId()).orElse(null);
                if (sgs != null) {
                    SubjectReportDTO subjectReportDTO = subjectReportDTOList.stream()
                            .filter(sr -> sr.getSubjectId().equals(sgs.getSubjectId()))
                            .findFirst()
                            .orElseGet(() -> {
                                SubjectReportDTO newSubjectReportDTO = new SubjectReportDTO(sgs.getSubjectId(), new ArrayList<>());
                                subjectReportDTOList.add(newSubjectReportDTO);
                                return newSubjectReportDTO;
                            });

                    GradeReportDTO gradeReportDTO = subjectReportDTO.getGrades().stream()
                            .filter(gr -> gr.getGrade().equals(sgs.getGrade()))
                            .findFirst()
                            .orElseGet(() -> {
                                GradeReportDTO newGradeReportDTO = new GradeReportDTO(sgs.getGrade(), new ArrayList<>());
                                subjectReportDTO.getGrades().add(newGradeReportDTO);
                                return newGradeReportDTO;
                            });

                    gradeReportDTO.getSemesterMarks().add(new SemesterMarkDTO(sgs.getSemester(), mark.getMark()));
                }
            }
            //create final response and integrate mark and grade for final response
            UpdateStudentReportResponseDTO responseDTO = new UpdateStudentReportResponseDTO();
            responseDTO.setId(studentReport.getId());
            responseDTO.setStudentId(studentReport.getUserId());
            responseDTO.setName(studentReport.getName());
            responseDTO.setUpdateBy(studentReport.getUpdateBy());
            responseDTO.setUpdateTime(studentReport.getUpdateTime());
            responseDTO.setStatus(studentReport.getStatus());
            responseDTO.setReport(subjectReportDTOList);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật học bạ thành công !", responseDTO);
        } catch (Exception e) {
            log.error("Error updating student report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình cập nhật học bạ của bạn. Vui lòng thử lại sau");
        }
    }

    @Override
    public ResponseData<StudentReportResponseDTO> findStudentReportById(Integer studentReportId, Authentication authentication) {
        try {
            //check exist user
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                log.error("User {} not found", user);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            //check exist student report
            Optional<StudentReport> optionalStudentReport = studentReportRepository.findById(studentReportId);
            if (optionalStudentReport.isEmpty()) {
                log.error("Student Report with ID {} not found", studentReportId);
                return new ResponseData<>(ResponseCode.C204.getCode(), "Học bạ không được tìm thấy !");
            }
            //check authorize user
            StudentReport studentReport = optionalStudentReport.get();
            User authenticatedUser = user.get();
            if (!studentReport.getUserId().equals(authenticatedUser.getId())) {
                log.error("User {} is not authorized to update StudentReport {}", authenticatedUser.getId(), studentReportId);
                return new ResponseData<>(ResponseCode.C201.getCode(), "Người dùng không được phép mở học bạ này !");
            }

            //map mark and grade to response
            List<StudentReportMark> reportMarks = studentReportMarkRepository.findByStudentReportId(studentReportId);
            List<SubjectReportDTO> subjectReportDTOList = new ArrayList<>();
            //map mark and grade to response
            for (StudentReportMark mark : reportMarks) {
                SubjectGradeSemester sgs = subjectGradeSemesterRepository.findById(mark.getSubjectGradeSemesterId()).orElse(null);
                if (sgs != null) {
                    SubjectReportDTO subjectReportDTO = subjectReportDTOList.stream()
                            .filter(sr -> sr.getSubjectId().equals(sgs.getSubjectId()))
                            .findFirst()
                            .orElseGet(() -> {
                                SubjectReportDTO newSubjectReportDTO = new SubjectReportDTO(sgs.getSubjectId(), new ArrayList<>());
                                subjectReportDTOList.add(newSubjectReportDTO);
                                return newSubjectReportDTO;
                            });

                    GradeReportDTO gradeReportDTO = subjectReportDTO.getGrades().stream()
                            .filter(gr -> gr.getGrade().equals(sgs.getGrade()))
                            .findFirst()
                            .orElseGet(() -> {
                                GradeReportDTO newGradeReportDTO = new GradeReportDTO(sgs.getGrade(), new ArrayList<>());
                                subjectReportDTO.getGrades().add(newGradeReportDTO);
                                return newGradeReportDTO;
                            });

                    gradeReportDTO.getSemesterMarks().add(new SemesterMarkDTO(sgs.getSemester(), mark.getMark()));
                }
            }
            //create final response and integrate mark and grade for final response
            StudentReportResponseDTO responseDTO = new StudentReportResponseDTO();
            responseDTO.setId(studentReport.getId());
            responseDTO.setStudentId(studentReport.getUserId());
            responseDTO.setName(studentReport.getName());
            responseDTO.setCreateBy(studentReport.getCreateBy());
            responseDTO.setCreateTime(studentReport.getCreateTime());
            responseDTO.setUpdateBy(studentReport.getUpdateBy());
            responseDTO.setUpdateTime(studentReport.getUpdateTime());
            responseDTO.setStatus(studentReport.getStatus());
            responseDTO.setReport(subjectReportDTOList);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Học bạ được tìm thấy !", responseDTO);
        } catch (Exception e) {
            log.error("Error finding student report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi trong quá trình mở học bạ. Vui lòng thử lại sau !");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseData<?> findAllStudentReports(String name, Pageable pageable, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            User authenticatedUser = user.get();
            Integer userId = authenticatedUser.getId();

            Page<StudentReport> studentReportsPage = studentReportRepository.findAll(userId, name, pageable);

            List<ListStudentReportResponse> listStudentReportResponse = studentReportsPage.stream().map(studentReport -> {
                ListStudentReportResponse responseDTO = new ListStudentReportResponse();
                responseDTO.setId(studentReport.getId());
                responseDTO.setName(studentReport.getName());
                responseDTO.setCreateTime(studentReport.getCreateTime());
                responseDTO.setStatus(studentReport.getStatus());
                return responseDTO;
            }).collect(Collectors.toList());

            Page<ListStudentReportResponse> result = new PageImpl<>(listStudentReportResponse, pageable, studentReportsPage.getTotalElements());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách học bạ đã được tải thành công!", result);
        } catch (Exception e) {
            log.error("Error retrieving student reports", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã có lỗi xảy ra trong quá trình tải danh sách học bạ. Vui lòng thử lại sau.");
        }
    }

    @Override
    public ResponseData<Void> deleteStudentReportById(Integer studentReportId, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            User authenticatedUser = user.get();

            log.info("Starting delete process for student report ID: {}", studentReportId);
            Optional<StudentReport> existStudentReport = studentReportRepository.findById(studentReportId);
            if (existStudentReport.isEmpty()) {
                log.error("Student Report with ID {} not found", studentReportId);
                return new ResponseData<>(ResponseCode.C204.getCode(), "Học bạ không tồn tại !");
            }

            StudentReport studentReport = existStudentReport.get();
            if (!studentReport.getUserId().equals(authenticatedUser.getId())) {
                log.error("User {} is not authorized to update StudentReport {}", authenticatedUser.getId(), studentReportId);
                return new ResponseData<>(ResponseCode.C201.getCode(), "Người dùng không được phép xóa học bạ này !");
            }

            if (studentReport == null || studentReport.getStatus().equals(StudentReportStatus.INACTIVE)) {
                log.warn("Student Report with ID: {} not found", studentReportId);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Học bạ không tồn tại !");
            }
            studentReport.setStatus(StudentReportStatus.INACTIVE);
            studentReportRepository.save(studentReport);
            log.info("Student Report with ID: {} is INACTIVE", studentReportId);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Xóa học bạ thành công !");
        } catch (Exception e) {
            log.error("Delete Student Report with ID failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Xóa học bạ thất bại, vui lòng thử lại sau !");
        }
    }
}