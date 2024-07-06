package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProvinceServiceImpl {
    private final ProvinceRepository provinceRepository;

    public Province findById(Integer id) throws ResourceNotFoundException{
        return provinceRepository.findById(id).orElseThrow(() -> {
            log.error("Province with id: {} not found.", id);
            return new ResourceNotFoundException("Địa chỉ cấp 1 với id: " + id + " không tìm thấy.");
        });
    }
}
