package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.StaffInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffInfoServiceImpl {
    private final StaffInfoRepository staffInfoRepository;

    public StaffInfo findStaffInfoById(Integer id){
        return staffInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Thông tin nhân viên không tìm thấy"));
    }
}
