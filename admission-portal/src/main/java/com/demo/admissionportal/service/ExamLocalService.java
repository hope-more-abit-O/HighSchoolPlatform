package com.demo.admissionportal.service;

import com.demo.admissionportal.ExamLocal;
import com.demo.admissionportal.dto.response.ResponseData;

import java.util.List;

public interface ExamLocalService {
    ResponseData<ExamLocal> createExamLocal(ExamLocal examLocal);
    ResponseData<ExamLocal> updateExamLocal(Integer id, ExamLocal examLocal);
    ResponseData<ExamLocal> getExamLocalById(Integer id);
    ResponseData<List<ExamLocal>> getAllExamLocals();
}