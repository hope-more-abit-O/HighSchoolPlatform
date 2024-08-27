package com.demo.admissionportal.service;

import com.demo.admissionportal.entity.ExamYear;
import com.demo.admissionportal.dto.response.ResponseData;

import java.util.List;

public interface ExamYearService {
    ResponseData<ExamYear> createExamYear(ExamYear examYear);
    ResponseData<ExamYear> updateExamYear(Integer id, ExamYear examYear);
    ResponseData<ExamYear> getExamYearById(Integer id);
    ResponseData<List<ExamYear>> getAllExamYears();
}