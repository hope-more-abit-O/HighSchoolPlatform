package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.Ward;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WardServiceImpl {
    private final WardRepository wardRepository;

    public Ward findById(Integer id) throws ResourceNotFoundException {
        return wardRepository.findById(id).orElseThrow(() -> {
            log.error("Ward with id: {} not found.", id);
            return new ResourceNotFoundException("Địa chỉ cấp 3 với id: " + id + " không tìm thấy.");
        });
    }
}
