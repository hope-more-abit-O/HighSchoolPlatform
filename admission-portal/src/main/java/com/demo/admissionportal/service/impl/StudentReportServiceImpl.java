package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SemesterType;
import com.demo.admissionportal.constants.StudentReportStatus;
import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.entity.student_report.GetHighSchoolExamSubjectScoreDTO;
import com.demo.admissionportal.dto.entity.student_report.GetStudentReportHighSchoolExamScoreDTO;
import com.demo.admissionportal.dto.entity.student_report.StudentReportHighSchoolExamScoreDTO;
import com.demo.admissionportal.dto.entity.subject_report_mark.SubjectMarkDTO;
import com.demo.admissionportal.dto.request.student_report.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.CreateStudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.ListStudentReportResponse;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.UpdateStudentReportResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.StudentReportMark;
import com.demo.admissionportal.entity.sub_entity.id.StudentReportHighSchoolScoreId;
import com.demo.admissionportal.entity.sub_entity.id.StudentReportMarkId;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.StudentReportHighSchoolScoreServiceImpl;
import com.demo.admissionportal.service.StudentReportService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentReportServiceImpl implements StudentReportService {
    private final UserRepository userRepository;
    private final StudentReportRepository studentReportRepository;
    private final SubjectGradeSemesterRepository subjectGradeSemesterRepository;
    private final StudentReportMarkRepository studentReportMarkRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectServiceImpl subjectServiceImpl;
    private final StudentReportHighSchoolScoreServiceImpl studentReportHighSchoolScoreService;

    public List<SubjectMarkDTO> getAverageScoreByStudentReportId(Integer studentReportId){
        List<SubjectMarkDTO> subjectMarkDTOS = new ArrayList<>();
        List<SubjectGradeSemester> subjectGradeSemesters = subjectGradeSemesterRepository.findBySemesterNot(SemesterType.AVERAGE);
        List<StudentReportMark> studentReportMarks = studentReportMarkRepository.findByStudentReportIdAndSubjectGradeSemesterIdIn(studentReportId, subjectGradeSemesters.stream().map(SubjectGradeSemester::getId).toList());
        if (studentReportMarks.isEmpty()){
            return null;
        }
        List<Integer> subjectIds = subjectGradeSemesters.stream().map(SubjectGradeSemester::getSubjectId).distinct().toList();
        for (Integer subjectId : subjectIds){
            List<SubjectGradeSemester> subjectGradeSemesterList = subjectGradeSemesters
                    .stream()
                    .filter((element) -> element.getSubjectId() == subjectId && element.getGrade() > 10)
                    .sorted(Comparator.comparing(SubjectGradeSemester::getGrade, Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();
            List<Float> scores = new ArrayList<>();

            List<SubjectGradeSemester> subjectGradeSemesterListGrade11 = subjectGradeSemesterList.stream().filter((element) -> element.getGrade() == 11).toList();
            List<Integer> subjectGradeSemesterGrade11Ids = subjectGradeSemesterListGrade11.stream().map(SubjectGradeSemester::getId).toList();
            List<StudentReportMark> studentReportMarkGrade11List = studentReportMarks
                    .stream()
                    .filter((element) -> subjectGradeSemesterGrade11Ids.contains(element.getSubjectGradeSemesterId()))
                    .toList();
            if (studentReportMarkGrade11List.get(0).getMark() == null || studentReportMarkGrade11List.get(1).getMark() == null){
                scores.add(null);
            } else {
                scores.add((studentReportMarkGrade11List.get(0).getMark() * 1 + studentReportMarkGrade11List.get(1).getMark() * 2)/3);
            }

            List<SubjectGradeSemester> subjectGradeSemesterListGrade12 = subjectGradeSemesterList.stream().filter((element) -> element.getGrade() == 12).toList();
            List<Integer> subjectGradeSemesterGrade2Ids = subjectGradeSemesterListGrade12.stream().map(SubjectGradeSemester::getId).toList();
            List<StudentReportMark> studentReportMarkGrade12List = studentReportMarks
                    .stream()
                    .filter((element) -> subjectGradeSemesterGrade2Ids.contains(element.getSubjectGradeSemesterId()))
                    .toList();
            if (studentReportMarkGrade12List.get(0).getMark() == null || studentReportMarkGrade12List.get(1).getMark() == null){
                scores.add(null);
            } else {
                scores.add((studentReportMarkGrade12List.get(0).getMark() * 1 + studentReportMarkGrade12List.get(1).getMark() * 2)/3);
            }

            if (scores.get(0) == null || scores.get(1) == null){
                subjectMarkDTOS.add(new SubjectMarkDTO(subjectId, null));
                continue;
            }
            Float sum = (scores.get(0) + scores.get(1))/2;
            subjectMarkDTOS.add(new SubjectMarkDTO(subjectId, sum));
        }

        return subjectMarkDTOS;
    }
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

                String subjectName = subjectRepository.findById(sgs.getSubjectId())
                        .map(Subject::getName)
                        .orElse(null);
                if (subjectName == null){
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy môn học này");
                }

                SubjectReportDTO subjectReportDTO = subjectReportDTOList.stream()
                        .filter(sr -> sr.getSubjectId().equals(sgs.getSubjectId()))
                        .findFirst()
                        .orElseGet(() -> {
                            SubjectReportDTO newSubjectReportDTO = new SubjectReportDTO(sgs.getSubjectId(), subjectName, new ArrayList<>());
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
            // Check exist user
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }

            // Check exist student report
            Optional<StudentReport> optionalStudentReport = studentReportRepository.findById(studentReportId);
            if (optionalStudentReport.isEmpty()) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Học bạ không được tìm thấy !");
            }

            // Check authorize user
            StudentReport studentReport = optionalStudentReport.get();
            User authenticatedUser = user.get();

            if (!studentReport.getUserId().equals(authenticatedUser.getId())) {
                log.info("User {} is not authorized to update StudentReport {}", authenticatedUser.getId(), studentReportId);
                return new ResponseData<>(ResponseCode.C201.getCode(), "Người dùng không được phép cập nhật học bạ này !");
            }

            // Update student report
            studentReport.setName(request.getStudentReportName());
            studentReport.setUpdateBy(authenticatedUser.getId());
            studentReport.setUpdateTime(new Date());
            if (request.getHighSchoolExamScore() != null) {
                studentReport.setHighSchoolExamScore(request.getHighSchoolExamScore());
            }
            if (request.getCompetencyAssessmentExamScore() != null) {
                studentReport.setCompetencyAssessmentExamScore(request.getCompetencyAssessmentExamScore());
            }

            studentReportRepository.save(studentReport);

            // Check valid grade, semester and subject exist in table subject_grade_semester
            for (UpdateMarkDTO markDTO : request.getMarks()) {
                List<SubjectGradeSemester> subjectGradeSemesters = subjectGradeSemesterRepository.findBySubjectIdAndGradeAndSemester(
                        markDTO.getSubjectId(), markDTO.getGrade(), markDTO.getSemester());

                if (markDTO.getMark() != null) {
                    if (markDTO.getMark() < 0 || markDTO.getMark() > 10) {
                        log.error("Invalid mark {} for Subject {}, Grade {}, Semester {}", markDTO.getMark(), markDTO.getSubjectId(), markDTO.getGrade(), markDTO.getSemester());
                        return new ResponseData<>(ResponseCode.C206.getCode(), "Điểm phải lớn hơn hoặc bằng 0 và bé hơn hoặc bằng 10 !");
                    }
                }

                if (subjectGradeSemesters.size() != 1) {
                    log.error("Expected one result but found {} for Subject {}, Grade {}, Semester {}",
                            subjectGradeSemesters.size(), markDTO.getSubjectId(), markDTO.getGrade(), markDTO.getSemester());
                    return new ResponseData<>(ResponseCode.C205.getCode(), "Môn học hoặc học kì hoặc khối lớp học không được tìm thấy hoặc trùng lặp !");
                }

                SubjectGradeSemester subjectGradeSemester = subjectGradeSemesters.get(0);

                StudentReportMarkId markId = new StudentReportMarkId(studentReport.getId(), subjectGradeSemester.getId());
                StudentReportMark studentReportMark = studentReportMarkRepository.findById(markId).orElse(new StudentReportMark());
                studentReportMark.setStudentReportId(studentReport.getId());
                studentReportMark.setSubjectGradeSemesterId(subjectGradeSemester.getId());
                studentReportMark.setMark(markDTO.getMark());
                log.info("Subject {}, Grade {}, Semester {} valid", markDTO.getSubjectId(), markDTO.getGrade(), markDTO.getSemester());
                studentReportMarkRepository.save(studentReportMark);
            }

            // Find student report marks to update
            List<StudentReportMark> reportMarks = studentReportMarkRepository.findByStudentReportId(studentReportId);
            List<SubjectReportDTO> subjectReportDTOList = new ArrayList<>();

            // Map mark and grade to response
            for (StudentReportMark mark : reportMarks) {
                SubjectGradeSemester sgs = subjectGradeSemesterRepository.findById(mark.getSubjectGradeSemesterId()).orElse(null);
                if (sgs != null) {
                    String subjectName = subjectRepository.findById(sgs.getSubjectId())
                            .map(subject -> subject.getName())
                            .orElse(null);
                    if (subjectName == null){
                        return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy môn học này");
                    }

                    SubjectReportDTO subjectReportDTO = subjectReportDTOList.stream()
                            .filter(sr -> sr.getSubjectId().equals(sgs.getSubjectId()))
                            .findFirst()
                            .orElseGet(() -> {
                                SubjectReportDTO newSubjectReportDTO = new SubjectReportDTO(sgs.getSubjectId(), subjectName, new ArrayList<>());
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

            // Create final response and integrate mark and grade for final response
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

    public StudentReport findById(Integer studentReportId) {
        return studentReportRepository.findById(studentReportId).orElse(null);
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
                    String subjectName = subjectRepository.findById(sgs.getSubjectId())
                            .map(Subject::getName)
                            .orElse(null);
                    if (subjectName == null){
                        return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy môn học này");
                    }

                    SubjectReportDTO subjectReportDTO = subjectReportDTOList.stream()
                            .filter(sr -> sr.getSubjectId().equals(sgs.getSubjectId()))
                            .findFirst()
                            .orElseGet(() -> {
                                SubjectReportDTO newSubjectReportDTO = new SubjectReportDTO(sgs.getSubjectId(), subjectName, new ArrayList<>());
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

            List<StudentReportHighSchoolScore> studentReportHighSchoolScores = studentReportHighSchoolScoreService.getByStudentReportId(studentReportId);
            List<SubjectDTO> subjectDTOs = subjectServiceImpl.getHighSchoolExamSubjects();
            List<GetStudentReportHighSchoolExamScoreDTO> studentReportHighSchoolExamScoreDTOS = new ArrayList<>();

            for (SubjectDTO subjectDTO : subjectDTOs) {
                studentReportHighSchoolExamScoreDTOS.add(new GetStudentReportHighSchoolExamScoreDTO(
                        studentReportHighSchoolScores
                                .stream()
                                .filter((element) -> element.getId().getSubjectId().equals(subjectDTO.getSubjectId()))
                                .findFirst()
                                .orElse(null),
                        subjectDTO
                        )
                );
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
            responseDTO.setHighSchoolExamSubjectScores(GetHighSchoolExamSubjectScoreDTO.mapping(studentReportHighSchoolExamScoreDTOS));

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

    public ResponseData<?> createHighSchoolExamScore(Integer studentReportId, UpdateHighSchoolExamScoreForStudentReportRequest request){
        StudentReport studentReport = findById(studentReportId);
        Integer userId = ServiceUtils.getId();

        if (!isStudentReportBelongToUser(studentReport, userId)) {
            throw new BadRequestException("Học bạ này không thuộc về bạn.");
        }

        if(isDuplicated(request.getScores())){
            throw new BadRequestException("Mã môn không được lặp.");
        }

        List<Integer> subjectIds = request.getScores().stream()
                .map(StudentReportHighSchoolExamScoreDTO::getSubjectId)
                .toList();

        List<Subject> subjects = subjectServiceImpl.findByIds(subjectIds);

        List<StudentReportHighSchoolScore> studentReportHighSchoolScores = new ArrayList<>();

        request.getScores().forEach((element) -> studentReportHighSchoolScores.add(new StudentReportHighSchoolScore(studentReportId, element)));

        studentReportHighSchoolScoreService.createStudentReportHighSchoolScore(studentReportHighSchoolScores);

        return ResponseData.ok("Lưu điểm thi THPT thành công.");
    }

    public ResponseData<?> updateHighSchoolExamScore(Integer studentReportId, UpdateHighSchoolExamScoreForStudentReportRequest request){
        StudentReport studentReport = findById(studentReportId);

        List<StudentReportHighSchoolScore> studentReportHighSchoolScoresExisted = studentReportHighSchoolScoreService.getByStudentReportIdAdnSubjectIdsIn(studentReportId,
                request.getScores().stream().map(StudentReportHighSchoolExamScoreDTO::getSubjectId).distinct().toList());

        if (request.getScores().size() != studentReportHighSchoolScoresExisted.size()) {
            throw new BadRequestException("Danh sách môn học không hợp lệ.");
        }

        Integer userId = ServiceUtils.getId();

        if (!isStudentReportBelongToUser(studentReport, userId)) {
            throw new BadRequestException("Học bạ này không thuộc về bạn.");
        }

        if(isDuplicated(request.getScores())){
            throw new BadRequestException("Mã môn không được lặp.");
        }

        List<Integer> subjectIds = request.getScores().stream()
                .map(StudentReportHighSchoolExamScoreDTO::getSubjectId)
                .toList();

        List<Subject> subjects = subjectServiceImpl.findByIds(subjectIds);

        List<StudentReportHighSchoolScore> studentReportHighSchoolScores = new ArrayList<>();

        request.getScores().forEach((element) -> studentReportHighSchoolScores.add(new StudentReportHighSchoolScore(studentReportId, element)));

        studentReportHighSchoolScoreService.createStudentReportHighSchoolScore(studentReportHighSchoolScores);

        return ResponseData.ok("Lưu điểm thi THPT thành công.");
    }

    public ResponseData<?> createHighSchoolExamScoreInStudentReport(Integer studentReportId ,UpdateHighSchoolExamScoreForStudentReportRequest request){
        isCreateAndUpdateHighSchoolExamScoreRequestValid(request);
        return createHighSchoolExamScore(studentReportId, request);
    }

    public ResponseData<?> updateHighSchoolExamScoreInStudentReport(Integer studentReportId ,UpdateHighSchoolExamScoreForStudentReportRequest request){
        isCreateAndUpdateHighSchoolExamScoreRequestValid(request);
        return updateHighSchoolExamScore(studentReportId, request);
    }

    public ResponseData<?> deleteHighSchoolExamScoreInStudentReport(Integer studentReportId){
        StudentReport studentReport = findById(studentReportId);
        Integer userId = ServiceUtils.getId();

        if (!isStudentReportBelongToUser(studentReport, userId)) {
            throw new BadRequestException("Học bạ này không thuộc về bạn.");
        }

        studentReportHighSchoolScoreService.deleteStudentReportHighSchoolScore(studentReportId);

        return ResponseData.ok("Xóa điểm thi THPT thành công.");
    }

    public boolean isDuplicated(List<StudentReportHighSchoolExamScoreDTO> scores){
        return scores.stream()
                .map(StudentReportHighSchoolExamScoreDTO::getSubjectId)
                .collect(Collectors.toSet())
                .size() != scores.size();
    }

    public void isCreateAndUpdateHighSchoolExamScoreRequestValid(UpdateHighSchoolExamScoreForStudentReportRequest request){
        List<Integer> mainSubjectIds = List.of(36,38,28);
        List<Integer> naturalScienceSubjectIds = List.of(16,23,27);
        List<Integer> socialScienceSubjectIds = List.of(9,54,34);
        Float naturalScienceScore = request.getScores().stream().filter((ele) -> naturalScienceSubjectIds.contains(ele.getSubjectId())).map(StudentReportHighSchoolExamScoreDTO::getScore).filter(Objects::nonNull).findFirst().orElse(null);
        Float socialScienceScore = request.getScores().stream().filter((ele) -> socialScienceSubjectIds.contains(ele.getSubjectId())).map(StudentReportHighSchoolExamScoreDTO::getScore).filter(Objects::nonNull).findFirst().orElse(null);

        if (naturalScienceScore == null && socialScienceScore == null){
            throw new BadRequestException("Bạn phải có điểm thi một trong hai khối: Khoa học Tự nhiên hoặc Khoa học Xã hội");
        } else if (naturalScienceScore != null && socialScienceScore != null){
            throw new BadRequestException("Bạn chỉ có thể có điểm thi một trong hai khối: Khoa học Tự nhiên hoặc Khoa học Xã hội");
        }

    }

    protected boolean isStudentReportBelongToUser(StudentReport studentReport, Integer userId) {
        return studentReport.getUserId().equals(userId);
    }
}