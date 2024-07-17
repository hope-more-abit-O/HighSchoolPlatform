package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistrictService {
    private final DistrictRepository districtRepository;

    public District findById(Integer id) throws ResourceNotFoundException {
        return districtRepository.findById(id).orElseThrow(() -> {
            log.error("District with id: {} not found.", id);
            return new ResourceNotFoundException("Địa chỉ cấp 2 với id: " + id + " không tìm thấy.");
        });
    }
}
