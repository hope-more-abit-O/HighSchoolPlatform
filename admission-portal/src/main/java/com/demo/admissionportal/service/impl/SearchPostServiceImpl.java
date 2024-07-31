package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.repository.SearchEngineRepository;
import com.demo.admissionportal.service.SearchPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPostServiceImpl implements SearchPostService {
    private final SearchEngineRepository searchEngineRepository;


    @Override
    public ResponseData<Page<PostSearchDTO>> searchPost(String title, String tag, String schoolName, String code, Pageable pageable) {
        try {
            log.info("Start retrieve search post");
            Page<PostSearchDTO> postRequestDTOS = searchEngineRepository.searchPost(title, tag, schoolName, code, pageable);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post ", postRequestDTOS);
        } catch (Exception ex) {
            log.info("Error when search post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post");
        }
    }
}
