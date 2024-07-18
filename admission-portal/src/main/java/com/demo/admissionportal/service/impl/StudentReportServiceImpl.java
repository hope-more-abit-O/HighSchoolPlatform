package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.GradeType;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.StudentReportStatus;
import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.request.student_report.GradeReportDTO;
import com.demo.admissionportal.dto.request.student_report.SemesterMarkDTO;
import com.demo.admissionportal.dto.request.student_report.SubjectReportDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.StudentReportMark;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.StudentReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentReportServiceImpl implements StudentReportService {
    private final StudentReportRepository studentReportRepository;
    private final StudentReportMarkRepository studentReportMarkRepository;
    private final SubjectGradeSemesterRepository subjectGradeSemesterRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseData<StudentReportResponseDTO> createStudentReport(CreateStudentReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isEmpty()) {
                log.error("User not found with username: {}", username);
                return new ResponseData<>(ResponseCode.C203.getCode(), "User not found.");
            }

            StudentReport existingReport = studentReportRepository.findByName(request.getStudentReportName().trim());
            if (existingReport != null) {
                log.warn("Student report already exists with name: {}", request.getStudentReportName().trim());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Student report already exists.");
            }

            User student = userOptional.get();
            Integer studentId = student.getId();
            StudentReport newStudentReport = new StudentReport();
            newStudentReport.setStudentId(studentId);
            newStudentReport.setName(request.getStudentReportName());
            newStudentReport.setStatus(StudentReportStatus.ACTIVE);
            newStudentReport.setCreateTime(new Date());
            studentReportRepository.save(newStudentReport);
            log.info("Saved new student report with ID: {}", newStudentReport.getId());

            List<SubjectReportDTO> subjectReportDTOList = new ArrayList<>();

            for (SubjectReportDTO subjectReport : request.getReport()) {
                Optional<Subject> subjectOptional = subjectRepository.findById(subjectReport.getSubjectId());
                if (subjectOptional.isEmpty()) {
                    log.warn("Subject not found with ID: {}", subjectReport.getSubjectId());
                    continue;
                }
                Subject subject = subjectOptional.get();
                List<GradeReportDTO> gradeReportDTOList = new ArrayList<>();
                for (GradeReportDTO gradeReport : subjectReport.getGrades()) {
                    List<SemesterMarkDTO> semesterMarkDTOList = new ArrayList<>();
                    for (SemesterMarkDTO semesterMark : gradeReport.getData()) {
                        log.debug("Attempting to find SubjectGradeSemester with subjectId: {}, grade: {}, semester: {}", subjectReport.getSubjectId(), gradeReport.getGrade(), semesterMark.getSemester());
                        Optional<SubjectGradeSemester> subjectGradeSemesterOptional = subjectGradeSemesterRepository.findBySubjectIdAndGradeAndSemester(
                                subjectReport.getSubjectId(),
                                gradeReport.getGrade(),
                                semesterMark.getSemester()
                        );
                        if (subjectGradeSemesterOptional.isPresent()) {
                            StudentReportMark mark = new StudentReportMark();
                            mark.setStudentReportId(newStudentReport.getId());
                            mark.setSubjectGradeSemesterId(subjectGradeSemesterOptional.get().getId());
                            mark.setMark(semesterMark.getMark());
                            studentReportMarkRepository.save(mark);
                            log.info("Saved mark for subject ID: {}, grade: {}, semester: {}", subjectReport.getSubjectId(), gradeReport.getGrade(), semesterMark.getSemester());

                            semesterMarkDTOList.add(new SemesterMarkDTO(
                                    semesterMark.getSemester(),
                                    semesterMark.getMark()
                            ));
                        } else {
                            log.warn("SubjectGradeSemester not found for subject ID: {}, grade: {}, semester: {}", subjectReport.getSubjectId(), gradeReport.getGrade(), semesterMark.getSemester());
                        }
                    }
                    gradeReportDTOList.add(new GradeReportDTO(
                            gradeReport.getGrade(),
                            semesterMarkDTOList
                    ));
                }
                subjectReportDTOList.add(new SubjectReportDTO(
                        subjectReport.getSubjectId(),
                        gradeReportDTOList
                ));
            }

            StudentReportResponseDTO responseDTO = new StudentReportResponseDTO();
            responseDTO.setId(newStudentReport.getId());
            responseDTO.setStudentId(newStudentReport.getStudentId());
            responseDTO.setName(newStudentReport.getName());
            responseDTO.setCreateBy(newStudentReport.getCreateBy());
            responseDTO.setUpdateTime(newStudentReport.getUpdateTime());
            responseDTO.setUpdateBy(newStudentReport.getUpdateBy());
            responseDTO.setCreateTime(newStudentReport.getCreateTime());
            responseDTO.setStatus(newStudentReport.getStatus());
            responseDTO.setReport(subjectReportDTOList);

            return new ResponseData<>(ResponseCode.C206.getCode(), "Student report created successfully.", responseDTO);
        } catch (Exception e) {
            log.error("Error occurred while creating student report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Internal server error.");
        }
    }
}
