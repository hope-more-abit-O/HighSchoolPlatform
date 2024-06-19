package com.demo.admissionportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaginationDTO<T> {
    private int pageNum;
    private int pageSize;
    private int totalPageNum;
    private long totalItems;
    private List<T> items;
}