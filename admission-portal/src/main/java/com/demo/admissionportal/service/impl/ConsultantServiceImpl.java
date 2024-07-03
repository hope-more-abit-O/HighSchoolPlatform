package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.consultant.ConsultantResponseDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.request.CreateConsultantRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.ConsultantInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ConsultantServiceImpl implements ConsultantInfoService {
    private final ConsultantInfoRepository consultantInfoRepository;
    private final ValidationServiceImpl validationServiceImpl;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UniversityInfoRepository universityInfoRepository;

    @Transactional
    public ResponseData createConsultant(CreateConsultantRequest request) throws DataExistedException {
        request.trim();

        validationServiceImpl.validateRegister(request.getUsername(), request.getEmail(), request.getPhone());

        Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        try {
            User consultant = userRepository.save(new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), Role.CONSULTANT, universityId));

            Optional<Province> province = provinceRepository.findById(request.getProvinceId());
            Optional<District> district = districtRepository.findById(request.getDistrictId());
            Optional<Ward> ward = wardRepository.findById(request.getWardId());

            if (Stream.of(province, district, ward).anyMatch(Optional::isEmpty)){
                return ResponseData.error("Tạo tư vấn viên thất bạn");
            }

            ConsultantInfo consultantInfo = consultantInfoRepository.save(new ConsultantInfo(
                consultant.getId(),
                universityId,
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getPhone(),
                request.getSpecificAddress(),
                Gender.valueOf(request.getGender()),
                province.get(),
                district.get(),
                ward.get()));

            User uniAccount = userRepository.findById(consultant.getCreateBy()).get();
            UniversityInfo uniInfo = universityInfoRepository.findById(uniAccount.getId()).get();

            return ResponseData.created("Tạo tư vấn viên thành công.", new ConsultantResponseDTO(consultant,
                    consultantInfo,
                    new InfoUniversityResponseDTO(uniAccount, uniInfo),
                    new ActionerDTO(uniAccount, uniInfo)));
        } catch (Exception e){
            return ResponseData.error("Tạo tư vấn viên thất bại.", e.getMessage());
        }
    }
}
