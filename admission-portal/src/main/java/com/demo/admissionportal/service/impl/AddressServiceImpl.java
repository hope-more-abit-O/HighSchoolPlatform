package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Region;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.UniversityCampusStatus;
import com.demo.admissionportal.dto.request.post.DistrictResponseDTO;
import com.demo.admissionportal.dto.request.post.WardResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.UniversityCampus;
import com.demo.admissionportal.entity.sub_entity.DistrictWard;
import com.demo.admissionportal.entity.sub_entity.ProvinceDistrict;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.ProvinceRepository;
import com.demo.admissionportal.repository.UniversityCampusRepository;
import com.demo.admissionportal.repository.sub_repository.DistrictWardRepository;
import com.demo.admissionportal.repository.sub_repository.ProvinceDistrictRepository;
import com.demo.admissionportal.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Province service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final ProvinceRepository provinceRepository;
    private final ProvinceDistrictRepository provinceDistrictRepository;
    private final DistrictWardRepository districtWardRepository;
    private final UniversityCampusRepository universityCampusRepository;

    /**
     * Find by id province.
     *
     * @param id the id
     * @return the province
     * @throws ResourceNotFoundException the resource not found exception
     */
    public Province findProvinceById(Integer id) throws ResourceNotFoundException {
        return provinceRepository.findById(id).orElseThrow(() -> {
            log.error("Province with id: {} not found.", id);
            return new ResourceNotFoundException("Địa chỉ cấp 1 với id: " + id + " không tìm thấy.");
        });
    }

    public List<Province> findProvinceByIds(List<Integer> ids) throws ResourceNotFoundException {
        return provinceRepository.findAllById(ids);
    }

    @Override
    public ResponseData<List<Province>> findProvince(List<String> regions) {
        List<Region> regionList = new ArrayList<>();
        List<Province> result;
        if (regions != null)
            try{
                for (String region : regions) {
                    regionList.add(Region.valueOf(region));
                }
            } catch (Exception e) {
                throw new BadRequestException("Region không hợp lệ", Map.of("region", e.getMessage()));
            }

        if (regionList.isEmpty()) {
            result = provinceRepository.findAll();
        }
        else
            result = provinceRepository.findByRegionIn(regionList);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách province", result);
    }

    @Override
    public ResponseData<List<DistrictResponseDTO>> findDistrict(Integer provinceId) {
        List<ProvinceDistrict> result = provinceDistrictRepository.findByProvinceId(provinceId);
        List<DistrictResponseDTO> districtResponseDTOS = new ArrayList<>();
        for (ProvinceDistrict provinceDistrict : result) {
            DistrictResponseDTO districtResponseDTO = new DistrictResponseDTO();
            districtResponseDTO.setId(provinceDistrict.getDistrict().getId());
            districtResponseDTO.setName(provinceDistrict.getDistrict().getName());
            districtResponseDTOS.add(districtResponseDTO);
        }
        return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách province và district", districtResponseDTOS);
    }

    @Override
    public ResponseData<List<WardResponseDTO>> findWard(Integer districtId) {
        List<DistrictWard> result = districtWardRepository.findByDistrictId(districtId);
        List<WardResponseDTO> wardResponseDTOS = new ArrayList<>();
        for (DistrictWard districtWard : result) {
            WardResponseDTO wardResponseDTO = new WardResponseDTO();
            wardResponseDTO.setId(districtWard.getWard().getId());
            wardResponseDTO.setName(districtWard.getWard().getName());
            wardResponseDTOS.add(wardResponseDTO);
        }
        return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách ward", wardResponseDTOS);
    }

    public List<Province> getAllProvince() {
        return provinceRepository.findAll();
    }

    public List<Province> getUniversityProvinces() {
        List<UniversityCampus> universityCampuses = universityCampusRepository.findByStatus(UniversityCampusStatus.ACTIVE);
        Set<Integer> provinceIds = universityCampuses.stream().map(UniversityCampus::getProvinceId).collect(Collectors.toSet());
        return provinceRepository.findAllById(provinceIds);
    }
}
