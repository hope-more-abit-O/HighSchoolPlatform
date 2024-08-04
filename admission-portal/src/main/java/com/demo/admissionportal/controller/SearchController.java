package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.SearchPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Search controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchPostService searchPostService;

    /**
     * Search post response entity.
     *
     * @param content  the content
     * @param pageable the pageable
     * @return the response entity
     */
    @PostMapping("/post")
    public ResponseEntity<ResponseData<Page<PostSearchDTO>>> searchPost(@RequestParam(required = true, name = "content") String content,
                                                                        @PageableDefault(size = 10) Pageable pageable) {
        ResponseData<Page<PostSearchDTO>> response = searchPostService.searchPost(content, pageable);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Search filter post response entity.
     *
     * @param content    the content
     * @param typeId     the type name
     * @param locationId the location id
     * @param startDate  the start date
     * @param endDate    the end date
     * @param authorId   the author
     * @param pageable   the pageable
     * @return the response entity
     */
    @PostMapping("/post/filter")
    public ResponseEntity<ResponseData<Page<PostSearchDTO>>> searchFilterPost(@RequestParam(required = false, name = "content") String content,
                                                                              @RequestParam(required = false, name = "typeId") List<Integer> typeId,
                                                                              @RequestParam(required = false, name = "locationId") List<Integer> locationId,
                                                                              @RequestParam(required = false, name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                              @RequestParam(required = false, name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                              @RequestParam(required = false, name = "authorId") List<Integer> authorId,
                                                                              @PageableDefault(size = 10) Pageable pageable) {
        ResponseData<Page<PostSearchDTO>> response = searchPostService.searchFilterPost(content, typeId, locationId, startDate, endDate, authorId, pageable);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}