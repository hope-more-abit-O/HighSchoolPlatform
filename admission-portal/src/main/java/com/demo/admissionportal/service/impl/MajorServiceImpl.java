package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.MajorStatus;
import com.demo.admissionportal.dto.entity.major.CreateMajorDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.dto.request.major.CreateMajorRequest;
import com.demo.admissionportal.dto.request.major.UpdateMajorRequest;
import com.demo.admissionportal.dto.request.major.UpdateMajorStatusRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.QueryException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.MajorRepository;
import com.demo.admissionportal.service.MajorService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import jakarta.transaction.Transactional;
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
        log.info("Find major by id: {}", id);
        return majorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ngành học không tìm thấy"));
    }
    public Major findByName(String name){
        return majorRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Ngành học không tìm thấy"));
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
            errors.put("majorNamesExisted", allNames);
        }

        if (!existedMajorsByCode.isEmpty()){
            String allCodes = existedMajorsByCode.stream()
                    .map(Major::getCode)
                    .collect(Collectors.joining(", "));
            errors.put("majorCodesExisted", allCodes);
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

    public Major getMajor(List<Major> majors, Integer id) throws ResourceNotFoundException {
        return majors.stream().filter(major -> major.getId().equals(id)).findFirst().orElse(null);
    }
    public String getMajorName(List<Major> majors, Integer id) throws ResourceNotFoundException {
        return majors.stream().filter(major -> major.getId().equals(id)).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành")).getName();
    }

    public Major findByCode(String code)
            throws ResourceNotFoundException{
        return majorRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("Ngành học không tìm thấy"));
    }
    public ResponseData<Page<Major>> getAllMajorsInfo(
            Pageable pageable,
            Integer id,
            String code,
            String name,
            String note,
            List<MajorStatus> status,
            Integer createBy,
            Integer updateBy,
            Date createTime,
            Date updateTime) {
        try {
            List<String> statusStrings = (status == null || status.isEmpty())
                    ? null
                    : status.stream().map(MajorStatus::name).toList();

            Page<Major> majors ;

            if (statusStrings != null){
                majors = majorRepository.findMajorBy(pageable, id, code, name, note,
                        statusStrings, createBy, updateBy, createTime, updateTime);
            } else {
                majors = majorRepository.findMajorBy(pageable, id, code, name, note,
                        createBy, updateBy, createTime, updateTime);
            }

            if (majors.getContent().isEmpty())
                return ResponseData.ok("Không tìm thấy chuyên ngành.");

            return ResponseData.ok("Lấy thông tin các chuyên ngành thành công.", majors);
        } catch (Exception e) {
            throw new QueryException(e.getMessage());
        }
    }

    @Override
    public InfoMajorDTO updateMajor(UpdateMajorRequest request) {
        try {
            Integer updaterId = ServiceUtils.getId();
            log.info("Updater ID: {}", updaterId);
            log.info("Received request to update major with ID: {}", request.getMajorId());

            Major major = this.findById(request.getMajorId());
            log.info("Found major: {}", major);

            major.update(request.getMajorName(), request.getMajorCode(), request.getNote(), updaterId);
            log.info("Updated major with new values from request.");

            Major savedMajor = majorRepository.save(major);
            log.info("Successfully saved updated major with ID: {}", savedMajor.getId());

            InfoMajorDTO infoMajorDTO = modelMapper.map(savedMajor, InfoMajorDTO.class);
            log.info("Mapped updated major to InfoMajorDTO: {}", infoMajorDTO);

            return infoMajorDTO;
        } catch (Exception e){
            log.error(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getCause().getCause().getStackTrace()) {
                log.warn(stackTraceElement.toString());
            }
            throw new QueryException(e.getMessage());
        }
    }

    @Override
    public ResponseData<InfoMajorDTO> updateMajorStatus(UpdateMajorStatusRequest request) {
        Integer updaterId = ServiceUtils.getId();
        log.info("Updater ID: {}", updaterId);
        log.info("Received request to update major with ID: {}", request.getMajorId());

        Major major = this.findById(request.getMajorId());
        log.info("Found major: {}", major);

        log.info("Comparing new status: {} with existed status: {}", request.getMajorStatus(), major.getStatus());
        if (major.getStatus().equals(request.getMajorStatus())){
            log.info("Status is the same.");
            return ResponseData.ok("Trạng thái giống nhau.", modelMapper.map(major, InfoMajorDTO.class));
        }

        major.update(request.getMajorStatus(), request.getNote(), updaterId);
        log.info("Updated major with new values from request.");

        Major savedMajor = majorRepository.save(major);
        log.info("Successfully saved updated major with ID: {}", savedMajor.getId());

        InfoMajorDTO infoMajorDTO = modelMapper.map(savedMajor, InfoMajorDTO.class);
        log.info("Mapped updated major to InfoMajorDTO: {}", infoMajorDTO);

        return ResponseData.ok("Cập nhập trạng thái thành công.", infoMajorDTO);
    }

    @Override
    @Transactional
    public InfoMajorDTO createMajor(CreateMajorRequest request){
        log.info("Start Transactional!");
        Integer id = ServiceUtils.getId();
        log.info("Get staff / admin's id: {}", id);

        log.info("Received request to create major with name: '{}' and code: '{}'", request.getMajorName(), request.getMajorCode());

        List<Major> majorsExisted = majorRepository.findByNameOrCode(request.getMajorName(), request.getMajorCode());

        Map<String, String> errors = new HashMap<>();

        if (!majorsExisted.isEmpty()){
            log.info("Found existing majors with the same name or code.");

            for (Major major : majorsExisted) {
                if ((major.getName().equals(request.getMajorName())) && (errors.get("majorName") == null)) {
                    log.error("Major name '{}' already exists.", request.getMajorName());
                    errors.put("majorName", "Tên ngành đã tồn tại.");
                }
                if ((major.getCode().equals(request.getMajorCode())) && (errors.get("code") == null)) {
                    log.error("Major code '{}' already exists.", request.getMajorCode());
                    errors.put("majorCode", "Mã ngành đã tồn tại.");
                }
            }

            log.error("Major already exists with errors: {}", errors);
            throw new DataExistedException("Ngành đã tồn tại.", errors);
        }

        try {
            log.info("Saving new major with name: '{}' and code: '{}'", request.getMajorName(), request.getMajorCode());
            Major savedMajor = majorRepository.save(new Major(request.getMajorName(), request.getMajorCode(), request.getNote(), id));
            log.info("Successfully saved new major with id: {}", savedMajor.getId());
            return modelMapper.map(savedMajor, InfoMajorDTO.class);
        } catch (Exception e){
            log.error("Error occurred while saving major: {}", e.getMessage(), e);
            throw new QueryException(e.getMessage());
        }
    }

}
