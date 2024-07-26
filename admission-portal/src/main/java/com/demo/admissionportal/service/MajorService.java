package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MajorStatus;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface MajorService {

    ResponseData<Page<Major>> getAllMajors(
            Pageable pageable,
            Integer id,
            String code,
            String name,
            String note,
            MajorStatus status,
            Integer createBy,
            Integer updateBy,
            Date createTime,
            Date updateTime);
}
