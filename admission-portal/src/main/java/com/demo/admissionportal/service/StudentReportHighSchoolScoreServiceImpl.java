package com.demo.admissionportal.service;

import com.demo.admissionportal.entity.StudentReportHighSchoolScore;
import com.demo.admissionportal.repository.StudentReportHighSchoolScoreRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class StudentReportHighSchoolScoreServiceImpl implements StudentReportHighSchoolScoreService {
    private final StudentReportHighSchoolScoreRepository studentReportHighSchoolScoreRepository;

    public void createStudentReportHighSchoolScore(List<StudentReportHighSchoolScore> studentReportHighSchoolScores) {
        studentReportHighSchoolScoreRepository.saveAll(studentReportHighSchoolScores);
    }

    public List<StudentReportHighSchoolScore> getByStudentReportId(Integer studentReportId) {
        return studentReportHighSchoolScoreRepository.findById_StudentReportId(studentReportId);
    }

    public List<StudentReportHighSchoolScore> getByStudentReportIdAdnSubjectIdsIn(Integer studentReportId, List<Integer> subjectIds) {
        return studentReportHighSchoolScoreRepository.findById_StudentReportIdAndId_SubjectIdIn(studentReportId, subjectIds);
    }

    public void updateStudentReportHighSchoolScore(List<StudentReportHighSchoolScore> studentReportHighSchoolScores) {
        studentReportHighSchoolScoreRepository.saveAll(studentReportHighSchoolScores);
    }

    public void deleteStudentReportHighSchoolScore(Integer studentReportId) {
        studentReportHighSchoolScoreRepository.deleteById_StudentReportId(studentReportId);
    }
}
