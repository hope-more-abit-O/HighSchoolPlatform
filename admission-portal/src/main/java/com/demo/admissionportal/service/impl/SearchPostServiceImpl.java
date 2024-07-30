package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.search_engine.SearchPostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.service.SearchPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPostServiceImpl implements SearchPostService {
    private final PostRepository postRepository;

    @Override
    public ResponseData<Page<Post>> searchPost(String title, String tag, String schoolName, String code, Pageable pageable) {
        try {
            log.info("Start retrieve search post");
            Page<Post> postRequestDTOS = postRepository.searchPost(title, tag, schoolName, code, pageable);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post ", postRequestDTOS);
        } catch (Exception ex) {
            log.info("Error when search post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post");
        }
    }
}
