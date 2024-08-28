package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.CampusType;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.UniversityCampusStatus;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusDTO;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusProperties;
import com.demo.admissionportal.dto.entity.university_campus.UniversityProperties;
import com.demo.admissionportal.dto.request.post.DistrictResponseDTO;
import com.demo.admissionportal.dto.request.post.ProvinceResponseDTO;
import com.demo.admissionportal.dto.request.post.WardResponseDTO;
import com.demo.admissionportal.dto.request.university_campus.CreateCampusRequestDTO;
import com.demo.admissionportal.dto.request.university_campus.UpdateCampusRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.university_campus.DeleteCampusResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.UniversityCampusService;
import com.demo.admissionportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final ValidationService validationService;
    private final ModelMapper modelMapper;

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
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy campus", universityCampusDTO);
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

    public List<UniversityCampusProperties> mapToListCampusV2(Integer universityId) {
        log.info("Start mapToListCampusV2");
        List<UniversityCampus> universityCampus = universityCampusRepository.findByUniversityId(universityId);
        List<Integer> provincesIds = universityCampus.stream().map(UniversityCampus::getProvinceId).distinct().toList();
        List<Integer> districtIds = universityCampus.stream().map(UniversityCampus::getDistrictId).distinct().toList();
        List<Integer> wardIds = universityCampus.stream().map(UniversityCampus::getWardId).distinct().toList();
        List<Province> provinces = provinceRepository.findAllById(provincesIds);
        List<District> districts = districtRepository.findAllById(districtIds);
        List<Ward> wards = wardRepository.findAllById(wardIds);
        return mapToCampus(universityCampus, provinces, districts, wards);
    }

    private UniversityCampusProperties mapToCampus(UniversityCampus universityCampus) {
        Ward wardCampus = wardRepository.findWardById(universityCampus.getWardId());
        Province provinceCampus = provinceRepository.findProvinceById(universityCampus.getProvinceId());
        District districtCampus = districtRepository.findDistrictById(universityCampus.getDistrictId());
        return UniversityCampusProperties.builder()
                .id(universityCampus.getId())
                .phone(universityCampus.getPhone())
                .campusName(universityCampus.getCampusName())
                .email(universityCampus.getEmail())
                .picture(mapToListPicture(universityCampus.getPicture()))
                .address(universityCampus.getSpecificAddress() + ", " + wardCampus.getName() + ", " + districtCampus.getName() + ", " + provinceCampus.getName())
                .specificAddress(universityCampus.getSpecificAddress())
                .provinceResponseDTO(modelMapper.map(provinceCampus, ProvinceResponseDTO.class))
                .wardResponseDTO(modelMapper.map(wardCampus, WardResponseDTO.class))
                .districtResponseDTO(modelMapper.map(districtCampus, DistrictResponseDTO.class))
                .type(universityCampus.getType().name)
                .status(universityCampus.getStatus().name)
                .build();
    }

    private UniversityCampusProperties mapToCampus(UniversityCampus universityCampus, Province province, District district, Ward ward) {
        return UniversityCampusProperties.builder()
                .id(universityCampus.getId())
                .phone(universityCampus.getPhone())
                .campusName(universityCampus.getCampusName())
                .email(universityCampus.getEmail())
                .picture(mapToListPicture(universityCampus.getPicture()))
                .address(universityCampus.getSpecificAddress() + ", " + ward.getName() + ", " + district.getName() + ", " + province.getName())
                .type(universityCampus.getType().name)
                .status(universityCampus.getStatus().name)
                .build();
    }

    private List<UniversityCampusProperties> mapToCampus(List<UniversityCampus> universityCampus, List<Province> provinces, List<District> districts, List<Ward> wards) {
        List<UniversityCampusProperties> result = new ArrayList<>();
        for (UniversityCampus uniCam : universityCampus) {
            Province province = provinces
                    .stream()
                    .filter((pr) -> pr.getId().equals(uniCam.getProvinceId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ cấp 1.", Map.of("provinceId", uniCam.getProvinceId().toString())));
            District district = districts
                    .stream()
                    .filter((ds) -> ds.getId().equals(uniCam.getDistrictId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ cấp 2.", Map.of("districtId", uniCam.getDistrictId().toString())));
            Ward ward = wards
                    .stream()
                    .filter((wr) -> wr.getId().equals(uniCam.getWardId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ cấp 3.", Map.of("wardId", uniCam.getWardId().toString())));

            result.add(mapToCampus(uniCam, province, district, ward));
        }
        return result;
    }

    private List<String> mapToListPicture(String picture) {
        return Arrays.asList(picture.split("\\s*,\\s*"));
    }


    @Override
    public ResponseData<UniversityCampus> createUniversityCampus(CreateCampusRequestDTO requestDTO) {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request null");
            }
            String picture = requestDTO.getPicture().stream()
                    .collect(Collectors.joining(","));
//            validationService.validateRegisterUniversityCampus(requestDTO.getEmail(), requestDTO.getPhone());
            UniversityCampus universityCampus = getUniversityCampus(requestDTO, universityId, picture);
            if (getUniversityCampus(requestDTO, universityId, picture) == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Cơ sở chính đã được đăng ký trước đó.");
            }
            UniversityCampus resultOfCreate = universityCampusRepository.save(universityCampus);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo campus thành công", resultOfCreate);
        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C205.getCode(), "Email, số điện thoại đã tồn tại");
        } catch (Exception ex) {
            log.error("Error when create university campus: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi tạo campus");
        }
    }

    @Override
    public ResponseData<DeleteCampusResponseDTO> deleteUniversityCampus(Integer id) {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            UniversityCampus resultOfDelete = universityCampusRepository.findUniversityCampusById(id);
            if (resultOfDelete == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy campus");
            }
            if (resultOfDelete.getStatus().equals(UniversityCampusStatus.ACTIVE)) {
                resultOfDelete.setStatus(UniversityCampusStatus.INACTIVE);
            } else {
                resultOfDelete.setStatus(UniversityCampusStatus.ACTIVE);
            }
            resultOfDelete.setUpdateBy(universityId);
            resultOfDelete.setUpdateTime(new Date());
            universityCampusRepository.save(resultOfDelete);
            DeleteCampusResponseDTO deleteCampusResponseDTO = new DeleteCampusResponseDTO();
            deleteCampusResponseDTO.setCurrentStatus(resultOfDelete.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Thay đổi trạng thái thành công", deleteCampusResponseDTO);
        } catch (Exception ex) {
            log.error("Error when delete university campus: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi xoá campus");
        }
    }

    @Override
    public ResponseData<UniversityCampusProperties> updateUniversityCampus(Integer campusId, UpdateCampusRequestDTO requestDTO) {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (requestDTO == null || campusId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request null");
            }
            UniversityCampus universityCampus = universityCampusRepository.findUniversityCampusById(campusId);
            if (universityCampus == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy campusId");
            }
            String picture = requestDTO.getPicture().stream()
                    .collect(Collectors.joining(","));


            UniversityCampus campus = getUpdateUniversityCampus(universityCampus, requestDTO, universityId, picture);
            if (campus == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Cơ sở chính đã được đăng ký trước đó. Vui lòng cập nhật lại");
            }
            UniversityCampus resultOfUpdate = universityCampusRepository.save(campus);
            if (resultOfUpdate != null) {
                var mapUniversity = modelMapper.map(resultOfUpdate, UniversityCampus.class);
                UniversityCampusProperties campusProperties = mapToCampusProperties(mapUniversity);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật thành công", campusProperties);
            }
            throw new Exception();
        } catch (DataExistedException de) {
            return new ResponseData<>(ResponseCode.C205.getCode(), "Email, số điện thoại đã tồn tại");
        } catch (Exception ex) {
            log.error("Error when update university campus: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi cập nhật campus");
        }

    }

    private UniversityCampusProperties mapToCampusProperties(UniversityCampus mapUniversity) {
        Ward wardCampus = wardRepository.findWardById(mapUniversity.getWardId());
        Province provinceCampus = provinceRepository.findProvinceById(mapUniversity.getProvinceId());
        District districtCampus = districtRepository.findDistrictById(mapUniversity.getDistrictId());
        return UniversityCampusProperties.builder()
                .id(mapUniversity.getId())
                .phone(mapUniversity.getPhone())
                .campusName(mapUniversity.getCampusName())
                .email(mapUniversity.getEmail())
                .picture(mapToListPicture(mapUniversity.getPicture()))
                .address(mapUniversity.getSpecificAddress() + ", " + wardCampus.getName() + ", " + districtCampus.getName() + ", " + provinceCampus.getName())
                .type(mapUniversity.getType().name)
                .status(mapUniversity.getStatus().name)
                .build();
    }

    private UniversityCampus getUpdateUniversityCampus(UniversityCampus universityCampus, UpdateCampusRequestDTO requestDTO, Integer universityId, String picture) {
        universityCampus.setCampusName(requestDTO.getCampusName() != null ? requestDTO.getCampusName() : universityCampus.getCampusName());
        if (requestDTO.getEmail() != null && !requestDTO.getEmail().equals(universityCampus.getEmail())) {
            universityCampus.setEmail(requestDTO.getEmail());
        }
        universityCampus.setSpecificAddress(requestDTO.getSpecificAddress() != null ? requestDTO.getSpecificAddress() : universityCampus.getSpecificAddress());
        universityCampus.setProvinceId(requestDTO.getProvinceId() != null ? requestDTO.getProvinceId() : universityCampus.getProvinceId());
        universityCampus.setDistrictId(requestDTO.getDistrictId() != null ? requestDTO.getDistrictId() : universityCampus.getDistrictId());
        universityCampus.setWardId(requestDTO.getWardId() != null ? requestDTO.getWardId() : universityCampus.getWardId());
        if (requestDTO.getPhone() != null && !requestDTO.getPhone().equals(universityCampus.getPhone())) {
            universityCampus.setPhone(requestDTO.getPhone());
        }
        universityCampus.setUpdateBy(universityId);
        universityCampus.setUpdateTime(new Date());
        universityCampus.setPicture(picture != null ? picture.trim() : universityCampus.getPicture());
        return universityCampus;
    }

    private UniversityCampus getUniversityCampus(CreateCampusRequestDTO requestDTO, Integer universityId, String picture) {
        UniversityCampus universityCampus = new UniversityCampus();
        universityCampus.setUniversityId(universityId);
        universityCampus.setCampusName(requestDTO.getCampusName());
        universityCampus.setEmail(requestDTO.getEmail());
        universityCampus.setSpecificAddress(requestDTO.getSpecificAddress());
        universityCampus.setProvinceId(requestDTO.getProvinceId());
        universityCampus.setDistrictId(requestDTO.getDistrictId());
        universityCampus.setWardId(requestDTO.getWardId());
        universityCampus.setPhone(requestDTO.getPhone());
        universityCampus.setCreateBy(universityId);
        universityCampus.setCreateTime(new Date());
        universityCampus.setPicture(picture.trim());
        boolean isExisted = checkHeadQuartersExisted(universityId);
        if (!isExisted) {
            universityCampus.setType(CampusType.HEADQUARTERS);
        } else {
            universityCampus.setType(CampusType.SUB_HEADQUARTERS);
        }
        universityCampus.setStatus(UniversityCampusStatus.ACTIVE);
        return universityCampus;
    }

    private boolean checkHeadQuartersExisted(Integer universityId) {
        boolean isExisted = false;
        UniversityCampus validateHeadQuarters = universityCampusRepository.findHeadQuartersCampusByUniversityId(universityId);
        return validateHeadQuarters == null ? isExisted : true;
    }


    @Override
    public ResponseData<String> changeTypeUniversityCampus(Integer campusID) {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            UniversityCampus universityCampus = universityCampusRepository.findHeadQuartersCampusByUniversityId(universityId);
            if (universityCampus.getId().equals(campusID)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Campus hiện tại là cơ sở chính");
            } else {
                UniversityCampus campus = universityCampusRepository.findUniversityCampusById(campusID);
                if (campus == null) {
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Campus Id không tồn tại");
                }
                // Update type to SUB_HEADQUARTERS
                universityCampus.setType(CampusType.SUB_HEADQUARTERS);
                universityCampus.setUpdateTime(new Date());
                universityCampus.setUpdateBy(universityId);
                universityCampusRepository.save(universityCampus);

                // Update type to HEADQUARTERS
                campus.setType(CampusType.HEADQUARTERS);
                campus.setUpdateTime(new Date());
                campus.setUpdateBy(universityId);
                universityCampusRepository.save(campus);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật trạng thái thành công");
            }

        } catch (Exception ex) {
            log.error("Failed to updates type university campus: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Cập nhật trạng thái thất bại", null);
        }
    }

    public List<UniversityCampus> findHeadQuarterByUniversityIds(List<Integer> uniIds) {
        return universityCampusRepository.findByUniversityIdInAndStatusAndType(uniIds, UniversityCampusStatus.ACTIVE, CampusType.HEADQUARTERS);
    }

    public List<UniversityCampus> findByUniversityIds(List<Integer> universityIds) {
        return universityCampusRepository.findByUniversityIdInAndStatus(universityIds, UniversityCampusStatus.ACTIVE);
    }
}
