package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.constants.UniversityMajorStatus;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.request.university_major.CreateUniversityMajorRequest;
import com.demo.admissionportal.dto.request.university_major.UpdateUniversityMajorStatusRequest;
import com.demo.admissionportal.dto.response.university_major.GetUniversityMajorResponse;
import com.demo.admissionportal.entity.ConsultantInfo;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.UniversityMajor;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.id.UniversityMajorId;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.UniversityMajorRepository;
import com.demo.admissionportal.service.resetPassword.UniversityMajorService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Service
@RequiredArgsConstructor
public class UniversityMajorServiceImpl implements UniversityMajorService {
    private final UniversityMajorRepository universityMajorRepository;
    private final MajorServiceImpl majorService;
    private final ConsultantInfoServiceImpl consultantInfoService;
    private final ModelMapper modelMapper;

    public List<UniversityMajor> getByUniversityId(Integer universityId) {
        return universityMajorRepository.findById_UniversityId(universityId);
    }

    public List<UniversityMajor> getByMajorId(Integer universityId) {
        return universityMajorRepository.findById_MajorId(universityId);
    }

    public void updateMajor(CreateUniversityMajorRequest request) {
        User user = ServiceUtils.getUser();
        List<UniversityMajor> newUniversityMajors;
        List<UniversityMajor> existedUniversityMajors;
        List<UniversityMajor> notIncludedUniversityMajors;
        List<UniversityMajor> universityMajors;

        if (user.getRole().equals(Role.UNIVERSITY)){
            universityMajors = this.getByUniversityId(user.getId());
        } else {
            universityMajors = this.getByUniversityId(user.getCreateBy());
        }


        existedUniversityMajors = universityMajors.stream().filter((ele) -> request.getMajorIds().contains(ele.getId().getMajorId())).toList();
        existedUniversityMajors.forEach((ele) -> ele.updateStatus(UniversityMajorStatus.ACTIVE, user.getId()));
        notIncludedUniversityMajors = universityMajors
                .stream()
                .filter((ele) -> !request.getMajorIds().contains(ele.getId().getMajorId()))
                .toList();
        notIncludedUniversityMajors.forEach((ele) -> ele.updateStatus(UniversityMajorStatus.INACTIVE, user.getId()));

        newUniversityMajors = request.getMajorIds()
                .stream()
                .filter((ele) -> !existedUniversityMajors.stream().map(UniversityMajor::getId).map(UniversityMajorId::getMajorId).distinct().toList().contains(ele))
                .map((ele) -> new UniversityMajor(user.getId(), ele, user.getId()))
                .toList();

        if (user.getRole().equals(Role.UNIVERSITY)){
            universityMajors = request.getMajorIds().stream().map((ele) -> new UniversityMajor(user.getId(), ele, user.getId())).toList();
        } else {
            ConsultantInfo consultantInfo = consultantInfoService.findById(user.getId());
            universityMajors = request.getMajorIds().stream().map((ele) -> new UniversityMajor(consultantInfo.getUniversityId(), ele, user.getId())).toList();
        }

        try {
            universityMajorRepository.saveAll(newUniversityMajors);
            universityMajorRepository.saveAll(notIncludedUniversityMajors);
            universityMajorRepository.saveAll(existedUniversityMajors);
        } catch (Exception e) {
            throw new StoreDataFailedException("Lưu các ngành hiện tại của trường thất bại.", Map.of("error", e.getCause().getMessage()));
        }
    }

    public GetUniversityMajorResponse getUniversityMajorByUniversityId(Integer universityId) {
        List<UniversityMajor> universityMajors = getByUniversityId(universityId);
        List<Major> majors = majorService.findByIds(universityMajors.stream().map(UniversityMajor::getId).map(UniversityMajorId::getMajorId).distinct().toList());
        return new GetUniversityMajorResponse(majors.stream().map((ele) -> modelMapper.map(ele, InfoMajorDTO.class)).toList());
    }

    public void updateUniversityMajorStatus(UpdateUniversityMajorStatusRequest request){
        List<Integer> majorIds = Stream.concat(request.getActiveMajorIds().stream(), request.getInactiveMajorIds().stream())
                .collect(Collectors.toList());
        majorService.findByIds(majorIds);

        User user = ServiceUtils.getUser();
        Integer uniId = null;

        if (user.getRole().equals(Role.UNIVERSITY)){
            uniId = user.getId();
        } else {
            uniId = user.getCreateBy();
        }

        List<UniversityMajor> universityMajors = this.getUniversityMajorByUniversityIdAndMajorIds(uniId, majorIds);

        if (majorIds.size() != universityMajors.size()){
            throw new StoreDataFailedException("Có ngành không được trường đăng ký.");
        }

        List<UniversityMajor> activeUniversityMajors = universityMajors.stream()
                .filter((ele) -> request.getActiveMajorIds().contains(ele.getId().getMajorId()))
                .toList();

        List<UniversityMajor> inactiveUniversityMajors = universityMajors.stream()
                .filter((ele) -> request.getInactiveMajorIds().contains(ele.getId().getMajorId()))
                .toList();
    }

    public List<UniversityMajor> getUniversityMajorByUniversityIdAndMajorIds(Integer universityId, List<Integer> majorIds) {
        return universityMajorRepository.findById_UniversityIdAndId_MajorIdIn(universityId, majorIds);
    }


}
