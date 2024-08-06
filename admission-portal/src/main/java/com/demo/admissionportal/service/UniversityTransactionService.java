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
     * @param adsPackage   the ads package
     * @return the university transaction
     */
    UniversityTransaction createTransaction(Integer universityId, AdsPackage adsPackage);

    /**
     * Update transaction university transaction.
     *
     * @param transactionId the transaction id
     * @param status        the status
     * @return the university transaction
     */
    UniversityTransaction updateTransaction(Integer transactionId, String status);

    /**
     * Find transaction university transaction.
     *
     * @param transactionId the transaction id
     * @return the university transaction
     */
    UniversityTransaction findTransaction(Integer transactionId);
}
