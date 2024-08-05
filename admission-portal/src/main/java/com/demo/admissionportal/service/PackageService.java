package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.ads_package.CreatePackageRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.ads_package.DeletePackageResponseDTO;
import com.demo.admissionportal.entity.AdsPackage;

import java.util.List;

/**
 * The interface Package service.
 */
public interface PackageService {
    /**
     * Create package response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<AdsPackage> createPackage(CreatePackageRequestDTO requestDTO);

    /**
     * Delete package response data.
     *
     * @param packageId the package id
     * @return the response data
     */
    ResponseData<DeletePackageResponseDTO> deletePackage(Integer packageId);

    /**
     * Gets packages.
     *
     * @return the packages
     */
    ResponseData<List<AdsPackage>> getPackages();
}
