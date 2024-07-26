package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.ConsultantInfo;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.ConsultantInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultantInfoServiceImpl {
    private final ConsultantInfoRepository consultantInfoRepository;

    public ConsultantInfo findById(Integer id)
            throws ResourceNotFoundException {
        return consultantInfoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin tư vấn viên."));
    }
}
