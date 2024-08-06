package com.demo.admissionportal.service;

import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityTransaction;

/**
 * The interface University package service.
 */
public interface UniversityPackageService {
    /**
     * Create uni package.
     *
     * @param universityTransaction the university transaction
     * @param postId                the post id
     */
    void createUniPackage(AdsPackage adsPackage, UniversityTransaction universityTransaction, Integer postId);
}
