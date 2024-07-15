package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.entity.major.CreateMajorDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MajorServiceImpl {
    private final MajorRepository majorRepository;
    private final ModelMapper modelMapper;

    public Major findById(int id){
        return majorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ngành học không tìm thấy"));
    }

    public List<Major> findByIds(List<Integer> ids)
            throws ResourceNotFoundException{
        List<Major> majors = majorRepository.findByIdIn(ids);

        // Check for IDs that were not found
        List<Integer> foundIds = majors.stream().map(Major::getId).toList();
        List<Integer> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

        Map<String, String> error = new HashMap<>();
        error.put("error", notFoundIds
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        if (!notFoundIds.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy ngành.", error);
        }

        return majors;
    }

    public boolean checkMajorName(String majorName) {
        return majorRepository.findByName(majorName).isPresent();
    }

    public Major save(Major major)
            throws StoreDataFailedException {
        try {
            return majorRepository.save(major);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin ngành học thất bại.");
        }
    }

    public List<Major> save(List<Major> majors)
            throws StoreDataFailedException {
        try {
            return majorRepository.saveAll(majors);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin ngành học thất bại.");
        }
    }

    public void checkForExistingMajorNamesAndCodes(List<CreateMajorDTO> createMajorDTOs)
            throws DataExistedException {
        Set<String> majorNames = createMajorDTOs.stream()
                .map(CreateMajorDTO::getName)
                .collect(Collectors.toSet());

        Set<String> majorCodes = createMajorDTOs.stream()
                .map(CreateMajorDTO::getCode)
                .collect(Collectors.toSet());

        List<Major> existingMajors = majorRepository.findByNameInOrCodeIn(majorNames, majorCodes);

        if (existingMajors.size() > 0){
            Set<String> existingMajorNames = existingMajors.stream()
                    .map(Major::getName)
                    .collect(Collectors.toSet());

            Set<String> existingMajorCodes = existingMajors.stream()
                    .map(Major::getCode)
                    .collect(Collectors.toSet());

            Map<String, String> errors = new HashMap<>();
            createMajorDTOs.forEach(major -> {
                if (existingMajorNames.contains(major.getName()) || existingMajorCodes.contains(major.getCode()))
                    errors.put("majorExisted", major.getName() + "-" + major.getCode());
            });

            if (!errors.isEmpty()) {
                throw new DataExistedException("Có phương thức tuyển sinh đã tồn tại", errors);
            }
        }
    }

    public List<Major> createAndSaveMajors(List<CreateMajorDTO> majorDTOs, Integer createById)
            throws StoreDataFailedException {
        List<Major> savedMajors = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();

        majorDTOs.forEach(majorDTO -> {
            try {
                Major major = majorRepository.save(new Major(majorDTO.getCode(), majorDTO.getName(), createById));
                savedMajors.add(major);
            } catch (Exception e) {
                // Log the error
                log.error("Error saving major: {} \n {}", majorDTO.getName(), e.getMessage());
                errors.put("error", majorDTO.getName() + "-" + majorDTO.getCode() + ": lưu thất bại");
            }
        });

        if (!errors.isEmpty()) {
            throw new StoreDataFailedException("Lưu thông tin phương thức tuyển sinh thất bại.", errors);
        }

        return savedMajors;
    }

    public List<Major> checkAndInsert(List<CreateMajorDTO> majors)
            throws DataExistedException, StoreDataFailedException {
        Integer createById = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        checkForExistingMajorNamesAndCodes(majors);

        return createAndSaveMajors(majors, createById);
    }

    public List<Major> insertNewMajorsAndGetExistedMajors(List<CreateMajorDTO> createMajorDTOs, List<Integer> majorIds)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException {
        return Stream.concat(checkAndInsert(createMajorDTOs).stream(), findByIds(majorIds).stream()).toList();
    }

    public List<InfoMajorDTO> toListInfoMajorDTO(List<Major> majors){
        return majors.stream()
                .map(major -> modelMapper.map(major, InfoMajorDTO.class))
                .toList();
    }
}
