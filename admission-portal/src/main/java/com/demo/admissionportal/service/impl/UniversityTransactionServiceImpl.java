package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.UniversityTransactionStatus;
import com.demo.admissionportal.dto.request.ads_package.PackageResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.PackageRepository;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UniversityPackageRepository;
import com.demo.admissionportal.repository.UniversityTransactionRepository;
import com.demo.admissionportal.service.UniversityTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type University transaction service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityTransactionServiceImpl implements UniversityTransactionService {
    private final UniversityTransactionRepository universityTransactionRepository;
    private final ModelMapper modelMapper;
    private final UniversityInfoRepository universityInfoRepository;
    private final PackageRepository packageRepository;

    public UniversityTransaction createTransaction(Integer universityId, AdsPackage adsPackage) {
        try {
            UniversityTransaction universityTransaction = new UniversityTransaction();
            universityTransaction.setUniversityId(universityId);
            universityTransaction.setPackageId(adsPackage.getId());
            universityTransaction.setCreateBy(universityId);
            universityTransaction.setCreateTime(new Date());
            universityTransaction.setStatus(UniversityTransactionStatus.PENDING);
            return universityTransactionRepository.save(universityTransaction);
        } catch (Exception ex) {
            log.error("Error when create transaction {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public UniversityTransaction updateTransaction(Integer transactionId, String status) {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            UniversityTransaction universityTransaction = findTransaction(transactionId);
            if (universityTransaction == null) {
                return null;
            }
            if (status.equals("CANCELLED")) {
                universityTransaction.setStatus(UniversityTransactionStatus.CANCELED);
            } else if (status.equals("PAID")) {
                universityTransaction.setStatus(UniversityTransactionStatus.PAID);
            }
            universityTransaction.setCompleteTime(new Date());
            universityTransaction.setUpdateBy(universityId);
            return universityTransactionRepository.save(universityTransaction);
        } catch (Exception ex) {
            log.error("Error when update transaction {}", ex.getMessage());
        }
        return null;
    }

    public UniversityTransaction findTransaction(Integer transactionId) {
        return universityTransactionRepository.findByTransactionId(transactionId);
    }

    @Override
    public ResponseData<List<PackageResponseDTO>> getListPackage() {
        try {
            List<UniversityTransaction> list = universityTransactionRepository.findAll();
            List<PackageResponseDTO> responseDTOList = list.stream()
                    .map(this::mapToPackageResponse)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách giao dịch thành công", responseDTOList);

        } catch (Exception ex) {
            log.error("Error when get transaction {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi lấy danh sách giao dịch");
        }
    }

    private PackageResponseDTO mapToPackageResponse(UniversityTransaction universityTransaction) {
        PackageResponseDTO responseDTO = modelMapper.map(universityTransaction, PackageResponseDTO.class);
        var result = modelMapper.map(responseDTO, PackageResponseDTO.class);
        responseDTO.setInfoUniversity(mapToInfoUniversity(universityTransaction.getUniversityId()));
        responseDTO.setInfoPackage(mapToInfoPackage(universityTransaction.getPackageId()));
        return result;
    }

    private PackageResponseDTO.InfoPackage mapToInfoPackage(Integer packageId) {
        AdsPackage adsPackage = packageRepository.findPackageById(packageId);
        return PackageResponseDTO.InfoPackage.builder()
                .packageId(adsPackage.getId())
                .packageName(adsPackage.getName())
                .build();
    }

    private PackageResponseDTO.InfoUniversity mapToInfoUniversity(Integer universityId) {
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityId);
        return PackageResponseDTO.InfoUniversity.builder()
                .id(universityInfo.getId())
                .name(universityInfo.getName())
                .build();
    }

}
