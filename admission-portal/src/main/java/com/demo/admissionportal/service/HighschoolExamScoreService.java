package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.request.UpdateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.HighschoolExamScoreResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface HighschoolExamScoreService {
    ResponseData<List<HighschoolExamScoreResponse>> findAllWithFilter(Integer identificationNumber);

    ResponseData<HighschoolExamScoreResponse> createExamScore(CreateHighschoolExamScoreRequest request);

    ResponseData<HighschoolExamScoreResponse> updateExamScore(Integer identificationNumber, UpdateHighschoolExamScoreRequest request);

    ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionForAGroup(String subjectGroup);

    ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionForBGroup(String subjectGroup);

    ResponseData<Map<String, Map<Float, Integer>>> getScoreDistributionForCGroup(String subjectGroup);
}