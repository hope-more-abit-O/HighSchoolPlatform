package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.service.SearchPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchPostService searchPostService;

    @PostMapping("/post")
    public ResponseEntity<ResponseData<Page<Post>>> searchPost(@RequestParam(required = false, name = "title") String title,
                                                               @RequestParam(required = false, name = "tag") String tag,
                                                               @RequestParam(required = false, name = "schoolName") String schoolName,
                                                               @RequestParam(required = false, name = "code") String code, Pageable pageable) {

        ResponseData<Page<Post>> response = searchPostService.searchPost(title, tag, schoolName, code, pageable);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
