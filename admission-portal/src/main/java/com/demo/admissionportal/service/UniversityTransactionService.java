package com.demo.admissionportal.service;

import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityTransaction;

/**
 * The interface University transaction service.
 */
public interface UniversityTransactionService {
    /**
     * Create transaction.
     *
     * @param universityId the university id
     * @param packageId    the package id
     */
    UniversityTransaction createTransaction(Integer universityId, AdsPackage adsPackage);
}
