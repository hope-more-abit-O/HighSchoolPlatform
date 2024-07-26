package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.MajorStatus;
import com.demo.admissionportal.dto.entity.major.CreateMajorDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.QueryException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.MajorRepository;
import com.demo.admissionportal.service.MajorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {
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

    public Major saveAll(Major major)
            throws StoreDataFailedException {
        try {
            return majorRepository.save(major);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin ngành học thất bại.");
        }
    }

    public List<Major> saveAll(List<Major> majors)
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

    public List<Major> checkAndInsertCreateMajorDTO(List<CreateMajorDTO> majors)
            throws DataExistedException, StoreDataFailedException {
        Integer createById = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        checkForExistingMajorNamesAndCodes(majors);

        return createAndSaveMajors(majors, createById);
    }

    public List<Major> insertNewMajorsAndGetExistedMajors(List<CreateMajorDTO> createMajorDTOs, List<Integer> majorIds)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException {
        return Stream.concat(checkAndInsertCreateMajorDTO(createMajorDTOs).stream(), findByIds(majorIds).stream()).toList();
    }

    public List<InfoMajorDTO> toListInfoMajorDTO(List<Major> majors){
        return majors.stream()
                .map(major -> modelMapper.map(major, InfoMajorDTO.class))
                .toList();
    }

    public void checkExistedNameAndCode(List<Major> majors)
            throws DataExistedException {
        Map<String, String> errors = new HashMap<>();
        Set<String> majorNames = majors.stream()
                .map(Major::getName)
                .collect(Collectors.toSet());

        Set<String> majorCodes = majors.stream()
                .map(Major::getCode)
                .collect(Collectors.toSet());

        List<Major> existedMajorsByName = majorRepository.findByNameIn(majorNames);
        List<Major> existedMajorsByCode = majorRepository.findByCodeIn(majorCodes);

        if (!existedMajorsByName.isEmpty()){
            String allNames = existedMajorsByName.stream()
                    .map(Major::getName)
                    .collect(Collectors.joining(", "));
            errors.put("nameExisted", allNames);
        }

        if (!existedMajorsByCode.isEmpty()){
            String allCodes = existedMajorsByCode.stream()
                    .map(Major::getCode)
                    .collect(Collectors.joining(", "));
            errors.put("codeExisted", allCodes);
        }

        if (!errors.isEmpty()) {
            throw new DataExistedException("Có ngành học đã tồn tại", errors);
        }
    }

    public List<Major> insertNewMajorsAndGetExistedMajors(List<CreateAdmissionQuotaRequest> quotas)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException{
        Integer consultantId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        //CREATE A LIST OF NEW MAJORS
        List<Major> newMajors = quotas.stream()
                .filter(quota -> quota.getMajorId() == null )
                .map(quota -> new Major(quota.getMajorCode(), quota.getMajorName(), consultantId))
                .toList();

        //VALIDATE MAJOR'S NAME AND CODE
        checkExistedNameAndCode(newMajors);

        //SAVE ALL NEW MAJORS INTO DATABASE
        List<Major> result = saveAll(newMajors);

        //GET ALL MAJORS EXISTED BY IDS
        result.addAll(findByIds(quotas
                .stream()
                .map(CreateAdmissionQuotaRequest::getMajorId)
                .filter(Objects::nonNull)
                .toList()
        ));

        return result;
    }

    public ResponseData<Page<Major>> getAllMajors(
            Pageable pageable,
            Integer id,
            String code,
            String name,
            String note,
            MajorStatus status,
            Integer createBy,
            Integer updateBy,
            Date createTime,
            Date updateTime) {
        try {
            Page<Major> majors = majorRepository.findMajorBy(pageable, id, code, name, note,
                    (status != null) ? status.name() : null, createBy, updateBy, createTime, updateTime);

            if (majors.getTotalElements() == 0)
                return ResponseData.ok("Không tìm thấy chuyên ngành.");

            return ResponseData.ok("Lấy thông tin các chuyên ngành thành công.", majors);
        } catch (Exception e) {
            throw new QueryException(e.getMessage());
        }
    }
}
