package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PackageStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.ads_package.CreatePackageRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.ads_package.DeletePackageResponseDTO;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.PackageRepository;
import com.demo.admissionportal.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * The type Package service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseData<AdsPackage> createPackage(CreatePackageRequestDTO requestDTO) {
        try {
            Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Request null");
            }
            AdsPackage mapPackage = modelMapper.map(requestDTO, AdsPackage.class);
            mapPackage.setCreateBy(adminId);
            mapPackage.setCreateTime(new Date());
            mapPackage.setStatus(PackageStatus.ACTIVE);
            AdsPackage adsPackage = packageRepository.save(mapPackage);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo gói quảng cáo thành công", adsPackage);
        } catch (Exception ex) {
            log.error("Error when occurring create package: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Lỗi khi tạo gói quảng cáo");
        }
    }

    @Override
    public ResponseData<DeletePackageResponseDTO> deletePackage(Integer packageId) {
        try {
            Integer adminId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (packageId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "packageId null");
            }
            AdsPackage adsPackage = packageRepository.findById(packageId).orElse(null);
            if (adsPackage == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy gói quảng cáo");
            }
            if (adsPackage.getStatus() == PackageStatus.ACTIVE) {
                adsPackage.setStatus(PackageStatus.INACTIVE);
            } else {
                adsPackage.setStatus(PackageStatus.ACTIVE);
            }
            adsPackage.setUpdateTime(new Date());
            adsPackage.setUpdateBy(adminId);
            AdsPackage result = packageRepository.save(adsPackage);
            DeletePackageResponseDTO responseDTO = new DeletePackageResponseDTO();
            responseDTO.setCurrentStatus(result.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Thay đổi trạng thái gói quảng cáo thành công", responseDTO);
        } catch (Exception ex) {
            log.error("Error when occurring delete package: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Lỗi khi thay đổi trạng thái gói quảng cáo");
        }
    }

    @Override
    public ResponseData<List<AdsPackage>> getPackages() {
        try {
            List<AdsPackage> adsPackage = packageRepository.findAll();
            if (adsPackage == null) {
                throw new Exception();
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách gói quảng cáo thành công", adsPackage);

        } catch (Exception ex) {
            log.error("Error when occurring get all package: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Lỗi khi lấy danh sách gói quảng cáo");
        }
    }
}
