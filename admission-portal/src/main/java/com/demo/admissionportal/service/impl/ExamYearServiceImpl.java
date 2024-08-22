package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.ExamYear;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.repository.ExamYearRepository;
import com.demo.admissionportal.service.ExamYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamYearServiceImpl implements ExamYearService {

    private final ExamYearRepository examYearRepository;

    @Override
    public ResponseData<ExamYear> createExamYear(ExamYear examYear) {
        ExamYear savedExamYear = examYearRepository.save(examYear);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Exam Year created successfully", savedExamYear);
    }

    @Override
    public ResponseData<ExamYear> updateExamYear(Integer id, ExamYear examYear) {
        Optional<ExamYear> existingExamYear = examYearRepository.findById(id);
        if (existingExamYear.isPresent()) {
            ExamYear updatedExamYear = existingExamYear.get();
            updatedExamYear.setYear(examYear.getYear());
            updatedExamYear.setUpdateTime(examYear.getUpdateTime());
            updatedExamYear.setUpdateBy(examYear.getUpdateBy());
            examYearRepository.save(updatedExamYear);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Exam Year updated successfully", updatedExamYear);
        } else {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Exam Year not found");
        }
    }

    @Override
    public ResponseData<ExamYear> getExamYearById(Integer id) {
        Optional<ExamYear> examYear = examYearRepository.findById(id);
        return examYear.map(value -> new ResponseData<>(ResponseCode.C200.getCode(), "Exam Year found", value))
                .orElseGet(() -> new ResponseData<>(ResponseCode.C203.getCode(), "Exam Year not found"));
    }

    @Override
    public ResponseData<List<ExamYear>> getAllExamYears() {
        List<ExamYear> examYears = examYearRepository.findAll();
        return new ResponseData<>(ResponseCode.C200.getCode(), "All Exam Years retrieved successfully", examYears);
    }
}