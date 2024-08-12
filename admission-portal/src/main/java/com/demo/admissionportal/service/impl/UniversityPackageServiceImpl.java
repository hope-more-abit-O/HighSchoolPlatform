package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.UniversityPackageStatus;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.campaign.CampaignStatusResponseDTO;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityPackage;
import com.demo.admissionportal.entity.UniversityTransaction;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.UniversityPackageRepository;
import com.demo.admissionportal.service.UniversityPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * The type University package service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityPackageServiceImpl implements UniversityPackageService {
    private final UniversityPackageRepository universityPackageRepository;

    @Override
    public UniversityPackage createUniPackage(AdsPackage adsPackage, UniversityTransaction universityTransaction, Integer postId) {
        try {
            Integer universityId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            UniversityPackage oldUniversityPackage = findOldUniversityPackage(universityId, postId);
            UniversityPackage universityPackage = new UniversityPackage();
            if (oldUniversityPackage != null) {
                universityPackage.setPostId(postId);
                universityPackage.setUniversityId(universityTransaction.getUniversityId());
                universityPackage.setUniversityTransactionId(universityTransaction.getId());
                universityPackage.setUniversityId(universityPackage.getUniversityId());
                universityPackage.setCreateTime(oldUniversityPackage.getCompleteTime());

                LocalDateTime currentDateTime = oldUniversityPackage.getCompleteTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                int daysToAdd = adsPackage.getViewBoostValue();
                LocalDateTime completeDateTime = currentDateTime.plusDays(daysToAdd);
                Date completeTime = Date.from(completeDateTime.atZone(ZoneId.systemDefault()).toInstant());
                universityPackage.setCompleteTime(completeTime);
                universityPackage.setStatus(UniversityPackageStatus.INACTIVE);
            } else {
                universityPackage.setPostId(postId);
                universityPackage.setUniversityId(universityTransaction.getUniversityId());
                universityPackage.setUniversityTransactionId(universityTransaction.getId());
                universityPackage.setUniversityId(universityPackage.getUniversityId());

                universityPackage.setCreateTime(new Date());
                LocalDateTime currentDate = LocalDateTime.now();

                // Get the number of days to add
                int daysToAdd = adsPackage.getViewBoostValue();

                // Add days to the current date
                LocalDateTime completeDate = currentDate.plusDays(daysToAdd);

                // Convert LocalDate to Date
                Date completeTime = Date.from(completeDate.atZone(ZoneId.systemDefault()).toInstant());
                universityPackage.setCompleteTime(completeTime);
                universityPackage.setStatus(UniversityPackageStatus.INACTIVE);
            }
            return universityPackageRepository.save(universityPackage);
        } catch (Exception ex) {
            log.error("Error when create uni package: {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public UniversityPackage updateUniversityPackage(Integer transactionId, Integer postId, Integer packageId) {
        try {
            UniversityPackage universityPackage = universityPackageRepository.findUniversityPackageByTransactionId(transactionId);
            universityPackage.setStatus(UniversityPackageStatus.ACTIVE);
            return universityPackageRepository.save(universityPackage);
        } catch (Exception ex) {
            log.error("Error when update uni package: {}", ex.getMessage());
        }
        return null;
    }

    public UniversityPackage findOldUniversityPackage(Integer universityId, Integer postId) {
        return universityPackageRepository.findOldUniversityPackage(universityId, postId);
    }

    @Override
    public ResponseData<CampaignStatusResponseDTO> changeStatus(Integer postId) {
        try {
            UniversityPackage universityPackage = universityPackageRepository.findCampaignByPostId(postId);
            if (universityPackage == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy postId");
            }
            if (universityPackage.getStatus().equals(UniversityPackageStatus.ACTIVE)) {
                universityPackage.setStatus(UniversityPackageStatus.INACTIVE);
            } else {
                universityPackage.setStatus(UniversityPackageStatus.ACTIVE);
            }
            UniversityPackage result = universityPackageRepository.save(universityPackage);
            CampaignStatusResponseDTO campaignStatusResponseDTO = new CampaignStatusResponseDTO();
            campaignStatusResponseDTO.setCurrentStatus(result.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Thay đổi trạng thái chiến dịch thành công", campaignStatusResponseDTO);
        } catch (Exception ex) {
            log.error("Error when change status of uni package: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi thay đổi trạng thái chiến dịch");
        }
    }
}
