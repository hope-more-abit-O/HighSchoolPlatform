//package com.demo.admissionportal.service.impl;
//
//import com.demo.admissionportal.entity.AdsPackage;
//import com.demo.admissionportal.entity.UniversityPackage;
//import com.demo.admissionportal.entity.UniversityTransaction;
//import com.demo.admissionportal.repository.UniversityPackageRepository;
//import com.demo.admissionportal.service.UniversityPackageService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
///**
// * The type University package service.
// */
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class UniversityPackageServiceImpl implements UniversityPackageService {
//    private final UniversityPackageRepository universityPackageRepository;
//
//    @Override
//    public void createUniPackage(AdsPackage adsPackage, UniversityTransaction universityTransaction, Integer postId) {
//        try {
//            UniversityPackage universityPackage = new UniversityPackage();
//            universityPackage.setPostId(postId);
//            universityPackage.setUniversityTransactionId(universityTransaction.getId());
//            universityPackage.setUniversityId(universityPackage.getUniversityId());
//            universityPackage.setCreateTime(new Date());
//
//            universityPackage.setCompleteTime();
//            universityPackageRepository.save(universityPackage);
//        } catch (Exception ex) {
//            log.error("Error when create uni package: ", ex.getMessage());
//        }
//    }
//}
