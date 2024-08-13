package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.ExamYearData;
import com.demo.admissionportal.dto.YearlyExamScoreResponse;
import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.request.UpdateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.HighschoolExamScoreResponse;
import com.demo.admissionportal.dto.response.ListExamScoreByYearResponse;
import com.demo.admissionportal.dto.response.ListExamScoreByYearResponseV2;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface HighschoolExamScoreService {
    ResponseData<List<HighschoolExamScoreResponse>> findAllWithFilter(Integer identificationNumber, Integer year);

    ResponseData<List<YearlyExamScoreResponse>> createExamScores(List<ExamYearData> examYearDataList);

    ResponseData<List<YearlyExamScoreResponse>> updateExamScores(Integer listExamScoreByYearId, List<ExamYearData> examYearDataList);

    ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionBySubjectGroup(String local, String subjectGroup);
    ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionBySubject(String local, String subjectName);
    ResponseData<Map<String, Map<String, Float>>> getScoreDistributionByLocal(String subjectName);
    ResponseData<List<HighschoolExamScoreResponse>> getAllTop100HighestScoreBySubject(String subjectName, String local);
    ResponseData<String> publishExamScores(Integer listExamScoreByYearId);
    ResponseData<Page<ListExamScoreByYearResponse>> getAllListExamScoresByYear(Pageable pageable);
    ResponseData<ListExamScoreByYearResponseV2> getListExamScoreById(Integer id, int page, int size);
}