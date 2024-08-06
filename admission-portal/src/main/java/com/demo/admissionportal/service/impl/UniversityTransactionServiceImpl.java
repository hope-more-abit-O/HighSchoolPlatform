package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.UniversityTransactionStatus;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityTransaction;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.UniversityTransactionRepository;
import com.demo.admissionportal.service.UniversityTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The type University transaction service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityTransactionServiceImpl implements UniversityTransactionService {
    private final UniversityTransactionRepository universityTransactionRepository;

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
            universityTransaction.setUpdateTime(new Date());
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
}
