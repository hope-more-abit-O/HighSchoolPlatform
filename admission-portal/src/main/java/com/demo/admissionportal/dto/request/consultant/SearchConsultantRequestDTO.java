package com.demo.admissionportal.dto.request.consultant;

import jakarta.validation.constraints.NotNull;

public class SearchConsultantRequestDTO {
    private String search;
    @NotNull(message = "Không được thiếu số trang")
    private int pageNumber;
    @NotNull(message = "Không được thiếu độ lớn của trang")
    private int pageSize;
}
