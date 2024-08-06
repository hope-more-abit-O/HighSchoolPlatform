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
    ResponseData<Page<HighschoolExamScoreResponse>> findAllWithFilter(Integer identificationNumber, Pageable pageable);

    ResponseData<HighschoolExamScoreResponse> createExamScore(CreateHighschoolExamScoreRequest request);

    ResponseData<HighschoolExamScoreResponse> updateExamScore(Integer identificationNumber, UpdateHighschoolExamScoreRequest request);
    ResponseData<Map<Float, Integer>> getScoreDistributionForA00Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForA01Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForA02Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForAGroup();

    ResponseData<Map<Float, Integer>> getScoreDistributionForB00Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForB03Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForB08Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForBGroup();


    ResponseData<Map<Float, Integer>> getScoreDistributionForC00Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForC03Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForC04Group();
    ResponseData<Map<Float, Integer>> getScoreDistributionForCGroup();
}