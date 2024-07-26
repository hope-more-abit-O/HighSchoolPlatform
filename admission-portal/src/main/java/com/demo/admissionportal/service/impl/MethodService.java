package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.MethodStatus;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Method;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface MethodService {
    ResponseData<Page<Method>> getAllMethods(
            Pageable pageable,
            Integer id,
            String code,
            String name,
            Date createTime,
            Integer createBy,
            Date updateTime,
            Integer updateBy,
            MethodStatus status);
}
