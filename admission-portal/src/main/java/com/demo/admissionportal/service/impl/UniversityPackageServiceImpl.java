package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.UniversityPackageStatus;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityPackage;
import com.demo.admissionportal.entity.UniversityTransaction;
import com.demo.admissionportal.repository.UniversityPackageRepository;
import com.demo.admissionportal.service.UniversityPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            UniversityPackage universityPackage = new UniversityPackage();
            universityPackage.setPostId(postId);
            universityPackage.setUniversityId(universityTransaction.getUniversityId());
            universityPackage.setUniversityTransactionId(universityTransaction.getId());
            universityPackage.setUniversityId(universityPackage.getUniversityId());
            universityPackage.setCreateTime(new Date());
            LocalDate currentDate = LocalDate.now();

            // Get the number of days to add
            int daysToAdd = adsPackage.getViewBoostValue();

            // Add days to the current date
            LocalDate completeDate = currentDate.plusDays(daysToAdd);

            // Convert LocalDate to Date
            Date completeTime = Date.from(completeDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            universityPackage.setCompleteTime(completeTime);

            universityPackage.setStatus(UniversityPackageStatus.INACTIVE);
            return universityPackageRepository.save(universityPackage);
        } catch (Exception ex) {
            log.error("Error when create uni package: ", ex.getMessage());
        }
        return null;
    }

    @Override
    public UniversityPackage updatePackage(Integer transactionId) {
        try {
            UniversityPackage universityPackage = universityPackageRepository.findUniversityPackageByTransactionId(transactionId);
            universityPackage.setStatus(UniversityPackageStatus.ACTIVE);
            return universityPackageRepository.save(universityPackage);
        } catch (Exception ex) {
            log.error("Error when update uni package: ", ex.getMessage());
        }
        return null;
    }
}
