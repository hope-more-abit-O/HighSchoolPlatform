package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusDTO;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusProperties;
import com.demo.admissionportal.dto.entity.university_campus.UniversityProperties;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.UniversityCampusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type University campus service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityCampusServiceImpl implements UniversityCampusService {
    private final UniversityCampusRepository universityCampusRepository;
    private final UniversityInfoRepository universityInfoRepository;
    private final UserRepository userRepository;
    private final WardRepository wardRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;

    @Override
    public ResponseData<UniversityCampusDTO> getUniversityCampus() {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (universityId == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy universityId");
            }
            UniversityCampusDTO universityCampusDTO = UniversityCampusDTO.builder()
                    .university(mapToUniversity(universityId))
                    .campus(mapToListCampus(universityId))
                    .build();
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy universityId ", universityCampusDTO);
        } catch (Exception ex) {
            log.error("Error when get campus by university id: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tìm kíếm campus");
        }
    }

    public UniversityProperties mapToUniversity(Integer universityId) {
        log.info("Start mapToUniversity");
        User user = userRepository.findUserById(universityId);
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityId);
        UniversityCampus campus = universityCampusRepository.findHeadQuartersCampusByUniversityId(universityId);
        Province campusAddress = provinceRepository.findProvinceById(campus.getProvinceId());
        UniversityProperties universityProperties = new UniversityProperties();
        universityProperties.setName(universityInfo.getName());
        universityProperties.setAvatar(user.getAvatar());
        universityProperties.setAddress(campusAddress.getName());
        log.info("End mapToUniversity");
        return universityProperties;
    }

    public List<UniversityCampusProperties> mapToListCampus(Integer universityId) {
        log.info("Start mapToListCampus");
        List<UniversityCampus> universityCampus = universityCampusRepository.findListUniversityCampusByUniversityId(universityId);
        return universityCampus.stream()
                .map(this::mapToCampus)
                .collect(Collectors.toList());
    }

    private UniversityCampusProperties mapToCampus(UniversityCampus universityCampus) {
        Ward wardCampus = wardRepository.findWardById(universityCampus.getWardId());
        Province provinceCampus = provinceRepository.findProvinceById(universityCampus.getProvinceId());
        District districtCampus = districtRepository.findDistrictById(universityCampus.getDistrictId());
        return UniversityCampusProperties.builder()
                .phone(universityCampus.getPhone())
                .campusName(universityCampus.getCampusName())
                .email(universityCampus.getEmail())
                .picture(mapToListPicture(universityCampus.getPicture()))
                .address(universityCampus.getSpecificAddress() + ", " + wardCampus.getName() + ", " + districtCampus.getName() + ", " + provinceCampus.getName())
                .type(universityCampus.getType().name)
                .build();
    }

    private List<String> mapToListPicture(String picture) {
        return Arrays.asList(picture.split("\\s*,\\s*"));
    }
}
