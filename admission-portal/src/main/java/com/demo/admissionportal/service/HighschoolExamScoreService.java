package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.request.UpdateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.HighschoolExamScoreResponse;
import com.demo.admissionportal.dto.response.ResponseData;

import java.util.List;
import java.util.Map;

public interface HighschoolExamScoreService {
    ResponseData<List<HighschoolExamScoreResponse>> findAllWithFilter(Integer identificationNumber);

    ResponseData<HighschoolExamScoreResponse> createExamScore(CreateHighschoolExamScoreRequest request);

    ResponseData<List<HighschoolExamScoreResponse>> updateExamScores(List<UpdateHighschoolExamScoreRequest> requests);

    ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionBySubjectGroup(String local, String subjectGroup);
    ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionBySubject(String local, String subjectName);
    ResponseData<Map<String, Map<String, Float>>> getScoreDistributionByLocal(String subjectName);
    ResponseData<List<HighschoolExamScoreResponse>> getAllTop100HighestScoreBySubject(String subjectName);

}