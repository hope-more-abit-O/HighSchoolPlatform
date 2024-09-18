package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.MajorStatus;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.request.major.CreateMajorRequest;
import com.demo.admissionportal.dto.request.major.UpdateMajorRequest;
import com.demo.admissionportal.dto.request.major.UpdateMajorStatusRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface MajorService {
    InfoMajorDTO createMajor(CreateMajorRequest request);

    ResponseData<Page<Major>> getAllMajorsInfo(
            Pageable pageable,
            Integer id,
            String code,
            String name,
            String note,
            List<MajorStatus> status,
            Integer createBy,
            Integer updateBy,
            Date createTime,
            Date updateTime);

    InfoMajorDTO updateMajor(UpdateMajorRequest request);

    ResponseData<InfoMajorDTO> updateMajorStatus(UpdateMajorStatusRequest request);
    List<InfoMajorDTO> findAll();
    List<Major> findByIds(List<Integer> ids);

    void createMajorRequest(CreateMajorRequest request);

    List<InfoMajorDTO> getAvailableMajors();
}
