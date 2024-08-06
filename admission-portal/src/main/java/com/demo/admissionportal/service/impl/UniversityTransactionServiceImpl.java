//package com.demo.admissionportal.service.impl;
//
//import com.demo.admissionportal.constants.UniversityTransactionStatus;
//import com.demo.admissionportal.entity.AdsPackage;
//import com.demo.admissionportal.entity.UniversityTransaction;
//import com.demo.admissionportal.repository.UniversityTransactionRepository;
//import com.demo.admissionportal.service.UniversityTransactionService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Date;
//
///**
// * The type University transaction service.
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UniversityTransactionServiceImpl implements UniversityTransactionService {
//    private final UniversityTransactionRepository universityTransactionRepository;
//
//    public UniversityTransaction createTransaction(Integer universityId, AdsPackage adsPackage) {
//        try {
//            UniversityTransaction universityTransaction = new UniversityTransaction();
//            universityTransaction.setUniversityId(universityId);
//            universityTransaction.setPackageId(adsPackage.getId());
//            universityTransaction.setCreateBy(universityId);
//            universityTransaction.setCreateTime(new Date());
//            LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN).plusHours(7);
//            universityTransaction.setStatus(UniversityTransactionStatus.PENDING);
//            return universityTransactionRepository.save(universityTransaction);
//        } catch (Exception ex) {
//            log.error("Error when create transaction {}", ex.getMessage());
//        }
//        return null;
//    }
//
//}
