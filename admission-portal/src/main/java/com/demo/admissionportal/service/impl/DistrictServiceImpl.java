package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistrictServiceImpl {
    private final DistrictRepository districtRepository;

    public List<District> getAll(){
        return districtRepository.findAll();
    }

    public District findById(Integer id) throws ResourceNotFoundException {
        return districtRepository.findById(id).orElseThrow(() -> {
            log.error("District with id: {} not found.", id);
            return new ResourceNotFoundException("Địa chỉ cấp 2 với id: " + id + " không tìm thấy.");
        });
    }

    public List<District> findByIds(List<Integer> ids){
        return districtRepository.findAllById(ids);
    }
}
