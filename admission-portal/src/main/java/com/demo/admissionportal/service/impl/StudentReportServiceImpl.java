package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.StudentReportStatus;
import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.request.student_report.GradeReportDTO;
import com.demo.admissionportal.dto.request.student_report.SemesterMarkDTO;
import com.demo.admissionportal.dto.request.student_report.SubjectReportDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.StudentReport;
import com.demo.admissionportal.entity.SubjectGradeSemester;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.entity.sub_entity.StudentReportMark;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.StudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentReportServiceImpl implements StudentReportService {
    private final StudentReportRepository studentReportRepository;
    private final StudentReportMarkRepository studentReportMarkRepository;
    private final SubjectGradeSemesterRepository subjectGradeSemesterRepository;
    private final SubjectRepository subjectRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseData<StudentReport> createStudentReport(CreateStudentReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "User not found.");
            }

            UserInfo userInfo = new UserInfo();
            Integer userInfoId = userInfo.getId();

            StudentReport existingReport = studentReportRepository.findByName(request.getStudentReportName().trim());
            if (existingReport != null) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Student report already exists.");
            }

            StudentReport newStudentReport = new StudentReport();
            newStudentReport.setStudentId(userInfoId);
            newStudentReport.setName(request.getStudentReportName());
            newStudentReport.setStatus(StudentReportStatus.ACTIVE);
            newStudentReport.setCreateTime(new Date());
            studentReportRepository.save(newStudentReport);

            for (SubjectReportDTO subjectReport : request.getReport()) {
                for (GradeReportDTO gradeReport : subjectReport.getGrades()) {
                    for (SemesterMarkDTO semesterMark : gradeReport.getData()) {
                        Optional<SubjectGradeSemester> subjectGradeSemesterOptional = subjectGradeSemesterRepository.findBySubjectIdAndGradeAndSemester(
                                subjectReport.getSubjectId(),
                                gradeReport.getGrade().name(),
                                semesterMark.getSemester()
                        );
                        if (subjectGradeSemesterOptional.isPresent()) {
                            StudentReportMark mark = new StudentReportMark();
                            mark.setStudentReportId(newStudentReport.getId());
                            mark.setSubjectGradeSemesterId(subjectGradeSemesterOptional.get().getId());
                            mark.setMark(semesterMark.getMark());
                            studentReportMarkRepository.save(mark);
                        }
                    }
                }
            }

            return new ResponseData<>(ResponseCode.C206.getCode(), "Student report created successfully.", newStudentReport);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.C207.getCode(), "Internal server error.");
        }
    }
}
