package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MethodStatus;
import com.demo.admissionportal.dto.entity.method.FullMethodDTO;
import com.demo.admissionportal.dto.request.method.PutMethodRequest;
import com.demo.admissionportal.dto.request.method.UpdateMethodStatusRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Method;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface MethodService {
    ResponseData getAllMethods(
            Pageable pageable,
            Integer id,
            String code,
            String name,
            Date createTime,
            Integer createBy,
            Date updateTime,
            Integer updateBy,
            List<MethodStatus> status);

    @Transactional
    ResponseData<Method> createMethod(String name, String code);

    ResponseData<Method> updateMethod(PutMethodRequest request);

    ResponseData<Method> updateMethodStatus(UpdateMethodStatusRequest request);
}
