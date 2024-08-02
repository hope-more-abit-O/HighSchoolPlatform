package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.repository.SearchEngineRepository;
import com.demo.admissionportal.service.SearchPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPostServiceImpl implements SearchPostService {
    private final SearchEngineRepository searchEngineRepository;

    @Override
    public ResponseData<Page<PostSearchDTO>> searchPost(String content, Pageable pageable) {
        try {
            log.info("Start retrieve search post");
            List<PostSearchDTO> postRequestDTOS = searchEngineRepository.searchPost(content);
            Sort.Order order = pageable.getSort().getOrderFor("create_time");
            if (order != null) {
                Comparator<PostSearchDTO> comparator = Comparator.comparing(PostSearchDTO::getCreateTime);
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                postRequestDTOS = postRequestDTOS.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), postRequestDTOS.size());
            Page<PostSearchDTO> page = new PageImpl<>(postRequestDTOS.subList(start, end), pageable, postRequestDTOS.size());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post ", page);
        } catch (Exception ex) {
            log.info("Error when search post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post");
        }
    }

    @Override
    public ResponseData<Page<PostSearchDTO>> searchFilterPost(Integer typeId, Integer locationId, LocalDate startDate, LocalDate endDate, Integer authorId, Pageable pageable) {
        try {
            log.info("Start retrieve search post by filter");
            List<PostSearchDTO> postRequestDTOS = searchEngineRepository.searchPostByFilter(typeId, locationId, startDate, endDate, authorId);
            Sort.Order order = pageable.getSort().getOrderFor("create_time");
            if (order != null) {
                Comparator<PostSearchDTO> comparator = Comparator.comparing(PostSearchDTO::getCreateTime);
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                postRequestDTOS = postRequestDTOS.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), postRequestDTOS.size());
            Page<PostSearchDTO> page = new PageImpl<>(postRequestDTOS.subList(start, end), pageable, postRequestDTOS.size());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post by filter", page);
        } catch (Exception ex) {
            log.info("Error when search post by filter: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post by filter");
        }
    }
}
