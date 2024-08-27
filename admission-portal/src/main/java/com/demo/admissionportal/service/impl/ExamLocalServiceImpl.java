package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.ExamLocal;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.repository.ExamLocalRepository;
import com.demo.admissionportal.service.ExamLocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamLocalServiceImpl implements ExamLocalService {

    private final ExamLocalRepository examLocalRepository;

    @Override
    public ResponseData<ExamLocal> createExamLocal(ExamLocal examLocal) {
        ExamLocal savedExamLocal = examLocalRepository.save(examLocal);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Địa phương đã được tạo mới.", savedExamLocal);
    }

    @Override
    public ResponseData<ExamLocal> updateExamLocal(Integer id, ExamLocal examLocal) {
        Optional<ExamLocal> existingExamLocal = examLocalRepository.findById(id);
        if (existingExamLocal.isPresent()) {
            ExamLocal updatedExamLocal = existingExamLocal.get();
            updatedExamLocal.setName(examLocal.getName());
            updatedExamLocal.setUpdateTime(examLocal.getUpdateTime());
            updatedExamLocal.setUpdateBy(examLocal.getUpdateBy());
            examLocalRepository.save(updatedExamLocal);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Exam Local updated successfully", updatedExamLocal);
        } else {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Địa phương không được tìm thấy.");
        }
    }

    @Override
    public ResponseData<ExamLocal> getExamLocalById(Integer id) {
        Optional<ExamLocal> examLocal = examLocalRepository.findById(id);
        return examLocal.map(value -> new ResponseData<>(ResponseCode.C200.getCode(), "Địa phương được tìm thấy", value))
                .orElseGet(() -> new ResponseData<>(ResponseCode.C203.getCode(), "Địa phương không được tìm thấy."));
    }

    @Override
    public ResponseData<List<ExamLocal>> getAllExamLocals() {
        List<ExamLocal> examLocals = examLocalRepository.findAll();
        return new ResponseData<>(ResponseCode.C200.getCode(), "Tất cả địa phương được tìm thấy", examLocals);
    }
}
