package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.campaign.CampaignStatusResponseDTO;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityPackage;
import com.demo.admissionportal.entity.UniversityTransaction;

/**
 * The interface University package service.
 */
public interface UniversityPackageService {
    /**
     * Create uni package.
     *
     * @param adsPackage            the ads package
     * @param universityTransaction the university transaction
     * @param postId                the post id
     * @return the university package
     */
    UniversityPackage createUniPackage(AdsPackage adsPackage, UniversityTransaction universityTransaction, Integer postId);

    /**
     * Update package university package.
     *
     * @param transactionId the transaction id
     * @param postId        the post id
     * @param packageId     the package id
     * @return the university package
     */
    UniversityPackage updateUniversityPackage(Integer transactionId, Integer postId, Integer packageId);

    /**
     * Change status campaign status response dto.
     *
     * @param postId the post id
     * @return the campaign status response dto
     */
    ResponseData<CampaignStatusResponseDTO> changeStatus(Integer postId);
}
