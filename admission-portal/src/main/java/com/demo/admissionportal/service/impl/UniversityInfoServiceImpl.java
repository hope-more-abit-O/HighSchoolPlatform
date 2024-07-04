package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.service.UniversityInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityInfoServiceImpl implements UniversityInfoService {
    private final UniversityInfoRepository universityInfoRepository;

    @Override
    public UniversityInfo findById(Integer id)throws ResourceNotFoundException {
        return universityInfoRepository.findById(id).orElseThrow(() -> {
            log.error("University's information with id: {} not found.", id);
            return new ResourceNotFoundException("University's information with id: " + id + " not found");
        });
    }
}
