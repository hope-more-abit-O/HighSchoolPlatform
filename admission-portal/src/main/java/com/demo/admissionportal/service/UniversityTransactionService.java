package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.ads_package.PackageResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.payment.OrderResponseDTO;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityTransaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    /**
     * Gets list package.
     *
     * @param adsName  the ads name
     * @param pageable the pageable
     * @return the list package
     */
    ResponseData<List<PackageResponseDTO>> getListPackage(String adsName, String status, Pageable pageable);

    /**
     * Gets order by uni id.
     *
     * @return the order by uni id
     */
    ResponseData<List<OrderResponseDTO>> getOrderByUniId();
}
